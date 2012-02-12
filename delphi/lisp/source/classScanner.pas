unit classScanner;

interface

uses
  declarations, classStateMachine, classState,
  Classes;


type
  TTokenArray = array of TToken;

type
  TScanner = class
  private
    procedure InitializeStateMachine;
    procedure Scan;
    function Contains(ch: Char; str: string): Boolean;
    function DoContainsCheck(ch: Char): Boolean;
    function DoPeekCheck(line, col: Integer): Boolean;
    function DoMagicPeekCheck(line, col: Integer): Boolean;
    procedure CreateToken(line: Integer);
    function GetTokenCount: Integer;
    function GetToken(i: Integer): TToken;
  protected
    FLines: TStringList;
    FTokens: TTokenArray;
    FStates: TStateMachine;
    FLog: string;
  public
    constructor Create(lines: TStringList);
    destructor Destroy; override;
    procedure Print;
    property Token[i: Integer]: TToken read GetToken;
    property Count: Integer read GetTokenCount;
  end;

implementation

uses
  SysUtils;

{ TScanner }

{
  Constructor.
    lines - TStringList containing program
}
constructor TScanner.Create(lines: TStringList);
begin
  inherited Create;
  FLines := lines;
  FStates := TStateMachine.Create;
  InitializeStateMachine;
  Scan;
end;

{
  Destructor.
}
destructor TScanner.Destroy;
begin
  FStates.Free;
  inherited Destroy;
end;

{
  Put TState object into FStates.
}
procedure TScanner.InitializeStateMachine;
begin
  FStates.Add(TState.Create(0, 0, cContains, CLParen, ttLParen, lNoLog));
  FStates.Add(TState.Create(0, 0, cContains, CRParen, ttRParen, lNoLog));
  FStates.Add(TState.Create(0, 0, cContains, CWhiteSpace, ttNone, lNoLog));
  FStates.Add(TState.Create(0, 0, cMagicPeek, CRParen, ttNone, lLog));
  FStates.Add(TState.Create(0, 0, cMagicPeek, CLParen, ttNone, lLog));
  FStates.Add(TState.Create(0, 0, cMagicPeek, CWhiteSpace, ttNone, lLog));
  FStates.Add(TState.Create(0, 1, cContains, CLetters, ttNone, lLog));
  FStates.Add(TState.Create(0, 2, cContains, CNumbers, ttNone, lLog));

  FStates.Add(TState.Create(1, 0, cContains, CWhiteSpace, ttIdentifier, lNoLog));
  FStates.Add(TState.Create(1, 0, cContains, CLParen, ttIdentifier, lNoLog));
  FStates.Add(TState.Create(1, 0, cContains, CRParen, ttIdentifier, lNoLog));
  FStates.Add(TState.Create(1, 0, cPeek, CRParen, ttIdentifier, lLog));
  FStates.Add(TState.Create(1, 0, cPeek, CLParen, ttIdentifier, lLog));
  FStates.Add(TState.Create(1, 0, cPeek, CWhiteSpace, ttIdentifier, lLog));
  FStates.Add(TState.Create(1, 1, cContains, CLetters, ttNone, lLog));
  FStates.Add(TState.Create(1, 1, cContains, CNumbers, ttNone, lLog));

  FStates.Add(TState.Create(2, 0, cContains, CWhiteSpace, ttNumber, lNoLog));
  FStates.Add(TState.Create(2, 0, cContains, CLParen, ttNumber, lNoLog));
  FStates.Add(TState.Create(2, 0, cContains, CRParen, ttNumber, lNoLog));
  FStates.Add(TState.Create(2, 0, cPeek, CRParen, ttNumber, lLog));
  FStates.Add(TState.Create(2, 0, cPeek, CLParen, ttNumber, lLog));
  FStates.Add(TState.Create(2, 0, cPeek, CWhiteSpace, ttNumber, lLog));
  FStates.Add(TState.Create(2, 2, cContains, CNumbers, ttNone, lLog));
end;

{
  Scan text for tokens.
}
procedure TScanner.Scan;
var
  line, col: Integer;
  ch: Char;
  success: Boolean;
begin
  for line := 0 to FLines.Count - 1 do
  begin
    for col := 1 to Length(FLines[line]) do
    begin
      success := False;
      ch := UpCase(FLines[line][col]);
      { try satisfying rules until one is successful }
      while not success do
      begin
        { see if character satisfies state's condition }
        case FStates.State.Check of
          cContains:  success := DoContainsCheck(ch);
          cPeek:      success := DoPeekCheck(line, col);
          cMagicPeek: success := DoMagicPeekCheck(line, col);
        end;
        { if condition satisfied and logging required, save character }
        if success and (FStates.State.Log = lLog)
          then FLog := FLog + ch;
        { if condition satisfied and tnew token is required, create the token }
        if success and (FStates.State.Token <> ttNone)
          then CreateToken(line);
        if success
          { if condition satisfied, set state to current's state .Next state }
          then FStates.SetNext
          else try
            { try to find another rule for the current state }
            FStates.FindNext;
          except
            { can't find another rule, raise exception }
            on E: Exception do
            begin
              raise Exception.Create(Format(
                'Scanner fubar at line %d, column %d', [line+1, col]
              ));
            end;
          end;
      end;
    end;
  end;
end;

{
  See if this character is allowed according to current state
    ch - character to check
}
function TScanner.DoContainsCheck(ch: Char): Boolean;
begin
  Result := Contains(ch, FStates.State.Allowed);
end;

{
  Check one character ahead
    line, col - current line and column
}
function TScanner.DoPeekCheck(line, col: Integer): Boolean;
var
  nextch: Char;
begin
  { there is no next character by default }
  nextch := #0;
  { if col is not the last character of the current line, nextchar is easy }
  if col < Length(FLines[line]) then
    nextch := UpCase(FLines[line][col+1]);
  { if last character of line }
  if col = Length(FLines[line]) then
  begin
    { if not the last line, next char is first char of next line }
    if line < FLines.Count - 1 then
      nextch := UpCase(FLines[line+1][1]);
  end;
  { rule is successful if nextchar is allowed according to current state }
  Result := Contains(nextch, FStates.State.Allowed);
end;

{
  Same as DoPeekCheck, but also sets .Token of current state to either
  ttIdentifier if current character is in CLetters, or sets .Token of current
  state to ttNumber if current character is in CNumbers.
    line, col - current line and column
}
function TScanner.DoMagicPeekCheck(line, col: Integer): Boolean;
var
  ch: char;
begin
  Result := DoPeekCheck(line, col);
  if Result then  
  begin
    ch := UpCase(FLines[line][col]);
    if Contains(ch, CLetters) then
      FStates.State.Token := ttIdentifier
    else
      if Contains(ch, CNumbers) then
        FStates.State.Token := ttNumber
      else
        raise Exception.Create(
          Format('Scanner fubar at line %d col %d', [line+1, col])
        );
  end;
end;

{
  Generate new token in FTokens array
    line - line number of token (for reporting errors)
}
procedure TScanner.CreateToken(line: Integer);
begin
  SetLength(FTokens, Length(FTokens)+1);
  FTokens[High(FTokens)].type_ := FStates.State.Token;
  FTokens[High(FTokens)].value := FLog;
  FTokens[High(FTokens)].line := line;
  FLog := '';
end;

{
  Check whether ch is contained in str. Should really use Pos???????!!
    ch - character
    str - string in which ch is to be found
}
function TScanner.Contains(ch: Char; str: string): Boolean;
{var
  i: Integer;}
begin
  Result := Pos(ch, str) > 0;
  {Result := False;
  for i := 1 to Length(str) do
    if str[i] = ch then
    begin
      Result := True;
      Break;
    end;}
end;

{
  Print all tokens in the FTokens array
}
procedure TScanner.Print;
var
  i: Integer;
begin
  for i := 0 to Length(FTokens) - 1 do
    if FTokens[i].Value <> '' then
      Writeln(Format('%d %s (%s)',
        [FTokens[i].line, CTokenStr[FTokens[i].type_], FTokens[i].value]
      ))
    else
      Writeln(Format('%d %s',
        [FTokens[i].line, CTokenStr[FTokens[i].type_]]
      ));
end;

{
  Getter for Token property
}
function TScanner.GetToken(i: Integer): TToken;
begin
  Result := FTokens[i];
end;

{
  Getter for Count property
}
function TScanner.GetTokenCount: Integer;
begin
  Result := Length(FTokens);
end;

end.
