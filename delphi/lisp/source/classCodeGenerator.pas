unit classCodeGenerator;

interface

uses
  Classes,
  classExpression, classAtom, classIdentifier, classNumber, classMolecule,
  classParser, classFunctionRegistry;

const
  CBuiltins: array[1..25, 0..2] of string =
    ( { source }  { header.inc }      { # of arguments }
      ( '+',      'builtin_add',      '2' ),
      ( '-',      'builtin_sub',      '2' ),
      ( '*',      'builtin_mul',      '2' ),
      ( '/',      'builtin_div',      '2' ),
      ( '%',      'builtin_mod',      '2' ),
      ( 'PRINT',  'builtin_print',    '1' ),
      ( 'IF',     'builtin_if',       '1' ),
      ( '&',      'builtin_bw_and',   '2' ),
      ( '|',      'builtin_bw_or',    '2' ),
      ( '^',      'builtin_bw_xor',   '2' ),
      ( '~',      'builtin_bw_not',   '1' ),
      ( '&&',     'builtin_log_and',  '2' ),
      ( '||',     'builtin_log_or',   '2' ),
      ( '~~',     'builtin_log_not',  '1' ),
      ( '<',      'builtin_lt',       '2' ),
      ( '>',      'builtin_gt',       '2' ),
      ( '=',      'builtin_eq',       '2' ),
      ( '<=',     'builtin_le',       '2' ),
      ( '>=',     'builtin_ge',       '2' ),
      ( '<>',     'builtin_ne',       '2' ),
      ( '<<',     'builtin_shl',      '2' ),
      ( '>>',     'builtin_shr',      '2' ),
      ( '~>',     'builtin_ror',      '2' ),
      ( '<~',     'builtin_rol',      '2' ),
      ( 'EAT',    'builtin_eat',      '1' )
    );

type
  TCodeGenerator = class
  private
    FCode: TStringList;
    FParser: TParser;
    FFuncReg: TFunctionRegistry;
    FFuncName: string;
    FCountIf: Integer;
    FCountLoop: Integer;
    FPushResult: Boolean;
    procedure GenerateExpression(e: TExpression);
    procedure GenerateAtom(a: TAtom);
    procedure GenerateParameter(i: TIdentifier);
    procedure GenerateConstant(n: TNumber);
    procedure GenerateMolecule(m: TMolecule);
    procedure GenerateFunction(m: TMolecule);
    procedure GenerateIf(m: TMolecule);
    procedure GenerateLoop(m: TMolecule);
    procedure GenerateFunctionCall(m: TMolecule);
    function LookupFunctionName(name: string): string;
    function LookupParamCount(name: string): Integer;
    procedure Code(const fmt: string; const args: array of const); overload;
    procedure Code(const str: string); overload;
    function SanitizeName(name: string): string;
  public
    constructor Create(parser: TParser);
    destructor Destroy; override;
    procedure Walk;
    procedure Print;
    procedure Save(filename: string);
  end;

implementation

uses
  SysUtils, declarations;

{ TCodeGenerator }

{
  Constructor
    parser - TParser object to generate code for
}
constructor TCodeGenerator.Create(parser: TParser);
begin
  inherited Create;
  FParser := parser;
  FFuncReg := TFunctionRegistry.Create;

  FCountLoop := 0;
  FCountIf := 0;

  FCode := TStringList.Create;
end;


{
  Destructor
}
destructor TCodeGenerator.Destroy;
begin
  FCode.Free;
  FFuncReg.Free;
  inherited Destroy;
end;

{
  Create code for a TNumber or for a TIdentifier
}
procedure TCodeGenerator.GenerateAtom(a: TAtom);
begin
  if a is TNumber then
    GenerateConstant(a as TNumber)
  else
    if a is TIdentifier then
      GenerateParameter(a as TIdentifier);
end;

{
  Create code for pushing a number on the stack
}
procedure TCodeGenerator.GenerateConstant(n: TNumber);
const
  line = #9'PUSHCONST %d';
begin
  Code(line, [n.Value]);
end;

{
  Create code for a TMolecule or for a TAtom
}
procedure TCodeGenerator.GenerateExpression(e: TExpression);
begin
  if e is TMolecule then
    GenerateMolecule(e as TMolecule)
  else
    if e is TAtom then
      GenerateAtom(e as TAtom);
    {TODO: else fubar;}
end;

{
  Create code for the declaration of a function.
    ugly hack #0:
      saves function name in instance var for looking up variables later
    ugly hack #1:
      will assign False to FPushResult when processing last expression
    ugly hack #2:
      temporarily save FPushResult in local var to restore it afterwards
}
procedure TCodeGenerator.GenerateFunction(m: TMolecule);
const
  FuncLabel = 'function_%s:';
  StartFunc = #9'BEGINF';
  EndFunc = #9'ENDF';
var
  i: Integer;
  n: TIdentifier;
  p: TMolecule;
  dopushresult: Boolean;
begin
  { get name atom and paramters molecule }
  n := TIdentifier(m.Exprs[0]);
  p := TMolecule(m.Exprs[1]);

  { get function name }
  FFuncName := n.Value;

  { put name + parameters in function registry }
  FFuncReg.RegisterFunction(FFuncName, p);

  { generate code for header of function }
  Code(FuncLabel, [SanitizeName(FFuncName)]);
  Code(StartFunc);

  { for every expression, push the result (in AX) onto the stack }
  FPushResult := True;
  for i := 2 to High(m.Exprs) do
    if i < High(m.Exprs) then
      GenerateExpression(m.Exprs[i])
    else
    begin
      dopushresult := FPushResult;
      FPushResult := False;
      GenerateExpression(m.Exprs[i]);
      FPushResult := dopushresult;
    end;
  Code(EndFunc);
  FFuncName := '';
end;

{
  Generate code that calls a function
    ugly hack #0:
      don't append PUSHRESULT code if FPushResult is false
    ugly hack #1:
      don't append PUSHRESULT if we're calling builtin_eat
}
procedure TCodeGenerator.GenerateFunctionCall(m: TMolecule);
const
  FuncCall = #9'CALL%d %s';
  PushResult = #9'PUSHRESULT';
var
  i: Integer;
  name: string;
  paramcount: Integer;
  dopushresult: Boolean;
begin
  { FPushResult hack }
  dopushresult := FPushResult;
  FPushResult := True;

  { generate code for every expression that's an argument to the function call }
  for i := High(m.Exprs) downto 0 do
    GenerateExpression(m.Exprs[i]);

  { find function name (may be a builtin function) }
  name := LookupFunctionName(TIdentifier(m.Atom).Value);

  { get number of parameters }
  paramcount := LookupParamCount(TIdentifier(m.Atom).Value);

  { generate code for function call }
  Code(FuncCall, [paramcount, name]);

  { don't push result (in AX) onto stack if dopushresult is False or if
    we're calling 'builtin_eat' }
  if dopushresult and (TIdentifier(m.Atom).Value <> 'EAT') then
    Code(PushResult);
end;

{
  Generate code for IF molecule.
    ugly hack #0 : adds a CLEAR3 statement to clean up after the builtin_if
                    call
}
procedure TCodeGenerator.GenerateIf(m: TMolecule);
const
  PushFalse = #9'PUSHCONST if_false_%d';
  PushTrue = #9'PUSHCONST if_true_%d';
  LabelFalse = 'if_false_%d:';
  LabelTrue = 'if_true_%d:';
  LabelDone = 'if_done_%d:';
  JumpDone = #9'jmp if_done_%d';
  CleanUp = #9'CLEAR3';
var
  i: Integer;
begin
  Inc(FCountIf);
  i := FCountIf;
  FPushResult := True;

  { generate code for pushing false and true labels onto stack }
  Code(PushFalse, [i]);
  Code(PushTrue, [i]);

  { generate code for condition expression }
  GenerateExpression(m.Exprs[0]);
  Code(#9'call builtin_if');

  { generate code for True expression }
  Code(LabelTrue, [i]);
  Code(CleanUp);
  GenerateExpression(m.Exprs[1]);
  Code(JumpDone, [i]);

  { generate code for False expression }
  Code(LabelFalse, [i]);
  Code(CleanUp);
  GenerateExpression(m.Exprs[2]);
  Code(LabelDone, [i]);
end;

{
  Generate code for a LOOP molecule.
}
procedure TCodeGenerator.GenerateLoop(m: TMolecule);
const
  LoopCount = #9'LOOPCOUNT %d';
  LoopStart = 'loop_start_%d:';
  LoopCounter = #9'SETCOUNTER%d';
  LoopEnd = #9'loop loop_start_%d';
var
  c, i: Integer;
  varname: string;
begin
  Inc(FCountLoop);
  c := FCountLoop;
  varname := TIdentifier(m.Exprs[1]).Value;
  Code(LoopCount, [TNumber(m.Exprs[0]).Value]);
  Code(LoopStart, [c]);
  Code(LoopCounter, [FFuncReg.GetParamNum(FFuncName, varname)]);
  for i := 2 to High(m.Exprs) do
    GenerateExpression(m.Exprs[i]);
  Code(LoopEnd, [c]);
end;

{
  Generate code for a molecule. Has special handlers for
    DEFUN, IF and LOOP functions.
}
procedure TCodeGenerator.GenerateMolecule(m: TMolecule);
var
  n: string;
begin
  if m.Atom is TIdentifier then
  begin
    n := TIdentifier(m.Atom).Value;
    if n = 'DEFUN' then
      GenerateFunction(m)
    else
      if n = 'IF' then
        GenerateIf(m)
      else
        if n = 'LOOP' then
          GenerateLoop(m)
        else
          GenerateFunctionCall(m);
  end;
  {TODO: else fubar;}
end;

{
  Generate code to load the value of a parameter from the stack
}
procedure TCodeGenerator.GenerateParameter(i: TIdentifier);
const
  GetParam = #9'GETP%d ax';
  PushParam = #9'PUSHRESULT';
var
  num: Integer;
begin
  num := FFuncReg.GetParamNum(FFuncName, i.Value);
  Code(GetParam, [num]);
  Code(PushParam);
end;

{
  For every expression in the TParser object, generate an expression in the
  FCode stringlist
}
procedure TCodeGenerator.Walk;
var
  i: Integer;
  lastdefun: Boolean;
begin
  Code(#9'jmp main');
  Code('');
  Code('INCLUDE header16.inc');
  Code('');

  lastdefun := False;
  for i := 0 to Length(FParser.Expressions) - 1 do
  begin
    if (not (FParser.Expressions[i] is TMolecule)) or
        (TIdentifier(TMolecule(FParser.Expressions[i]).Atom).Value <> 'DEFUN')
    then
      if lastdefun = False then
      begin
        Code('main:');
        lastdefun := True;
      end;
    FPushResult := False;
    GenerateExpression(FParser.Expressions[i]);
  end;

  Code(#9'QUIT');
end;

{
  Generate the mangled name for a function, either builtin_foo or function_foo
}
function TCodeGenerator.LookupFunctionName(name: string): string;
var
  i: Integer;
begin
  Result := '';
  for i := Low(CBuiltins) to High(CBuiltins) do
    if name = CBuiltins[i][0] then
      Result := CBuiltins[i][1];
  if Result = '' then
    Result := 'function_' + SanitizeName(name);
end;

{
  Get number of parameters so the stack can be cleaned up
}
function TCodeGenerator.LookupParamCount(name: string): Integer;
var
  i: Integer;
begin
  Result := -1;
  for i := Low(CBuiltins) to High(CBuiltins) do
    if name = CBuiltins[i][0] then
      Result := StrToInt(CBuiltins[i][2]);
  if Result = -1 then
    Result := FFuncReg.GetParamCount(name);
end;

{
  Add a line of code
}
procedure TCodeGenerator.Code(const fmt: string; const args: array of const);
begin
  FCode.Add(Format(fmt, args));
end;

{
  Add a line of code
}
procedure TCodeGenerator.Code(const str: string);
begin
  FCode.Add(str);
end;

{
  Print code onto stdout
}
procedure TCodeGenerator.Print;
var
  i: Integer;
begin
  for i := 0 to FCode.Count - 1 do
    WriteLn(FCode[i]);
end;

{
  Remove stupid characters from func names & replace with ascii code
}
function TCodeGenerator.SanitizeName(name: string): string;
var
  i: Integer;
begin
  Result := '';
  for i := 1 to Length(name) do
    if Pos(name[i], CWeirdLetters) > 0 then
      Result := Result + '_' + IntToStr(Ord(name[i])) + '_'
    else
      Result := Result + name[i];
end;

{
  Save code to some file
}
procedure TCodeGenerator.Save(filename: string);
begin
  FCode.SaveToFile(filename);
end;

end.
