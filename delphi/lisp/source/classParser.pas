unit classParser;

interface

uses
  declarations, classScannerIterator,
  classExpression, classMolecule, classAtom, classNumber, classIdentifier;

type
  TParser = class
  private
    FTokenIter: TScannerIterator;
    FExpressions: TExprArray;
    procedure Fubar;
    procedure ParseProgram;
    function ParseExpression: TExpression;
    function ParseMolecule: TMolecule;
    function ParseAtom: TAtom;
    function ParseNumber: TNumber;
    function ParseIdentifier: TIdentifier;
  public
    constructor Create(tokeniter: TScannerIterator);
    destructor Destroy; override;
    procedure Print;
    property Expressions: TExprArray read FExpressions;
  end;

implementation

uses
  SysUtils;

{ TParser }

{
  Constructor
    tokeniter - TTokenIter object that handles TScanner object
}
constructor TParser.Create(tokeniter: TScannerIterator);
begin
  inherited Create;
  FTokenIter := tokeniter;
  ParseProgram;
end;

{
  Destructor
}
destructor TParser.Destroy;
var
  i: Integer;
begin
  for i := 0 to High(FExpressions) do
    FExpressions[i].Free;
  SetLength(FExpressions, 0);
  inherited Destroy;
end;

{
  Call ParseExpression as long as there are more tokens in the iterator.
}
procedure TParser.ParseProgram;
begin
  while FTokenIter.HasMore do
  begin
    FTokenIter.NextToken;
    SetLength(FExpressions, Length(FExpressions)+1);
    FExpressions[High(FExpressions)] := ParseExpression;
  end;
end;

{
  Return either a TMolecule or a TAtom object
}
function TParser.ParseExpression: TExpression;
begin
  Result := nil;
  case FTokenIter.Current.type_ of
    ttLParen:
      Result := ParseMolecule;
    ttNumber, ttIdentifier:
      Result := ParseAtom;
    ttRParen:
      Result := nil;
    else Fubar;
  end;
end;

{
  Return a TIdentifier or a TNumber object
}
function TParser.ParseAtom: TAtom;
begin
  Result := nil;
  case FTokenIter.Current.type_ of
    ttIdentifier:
      Result := ParseIdentifier;
    ttNumber:
      Result := ParseNumber;
    else Fubar;
  end;
end;

{
  Return a TMolecule with a TIdentifier as .Value and TExpressions for .Exprs
}
function TParser.ParseMolecule: TMolecule;
var
  atom: TAtom;
  expr: TExpression;
  exprs: TExprArray;
begin
  FTokenIter.NextToken;

  { parse .Atom value for Molecule }
  atom := ParseAtom;

  { parse .Exprs for Molecule }
  SetLength(exprs, 0); { TODO: shouldn't this be a bug?
    Because exprs is stored in the TMolecule but is created on stack? }
  repeat
    if FTokenIter.HasMore then
      FTokenIter.NextToken
    else Fubar; { probably unterminated Molecule }
    expr := ParseExpression;
    { try to parse one more expression }
    if expr <> nil then
    begin
      SetLength(exprs, Length(exprs)+1);
      exprs[High(exprs)] := expr;
    end;
  until expr = nil;

  Result := TMolecule.Create(atom, exprs);
end;

{
  Create a TIdentifier for the current token
}
function TParser.ParseIdentifier: TIdentifier;
begin
  Result := TIdentifier.Create(FTokenIter.Current.value);
end;

{
  Createa a TNumber for the current token
}
function TParser.ParseNumber: TNumber;
begin
  Result := TNumber.Create(FTokenIter.Current.value);
end;

{
  Raise error condition
}
procedure TParser.Fubar;
begin
  raise Exception.Create(
    Format('Parser fubar at line %d', [FTokenIter.Current.line])
  );
end;

{
  Print all expressions (this will print entire parse tree)
}
procedure TParser.Print;
var
  i: Integer;
begin
  for i := 0 to High(FExpressions) do
  begin
    FExpressions[i].Print('');
    Writeln;
  end;
end;

end.
