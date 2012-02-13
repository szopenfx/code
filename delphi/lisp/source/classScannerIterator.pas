unit classScannerIterator;

interface

uses
  declarations, classScanner;

type
  TScannerIterator = class
  private
    FScanner: TScanner;
    FCurrent: Integer;
  public
    constructor Create(scanner: TScanner);
    procedure NextToken;
    function HasMore: Boolean;
    function Current: TToken;
    procedure Reset;
  end;

implementation

{ TScannerIterator }

{
  Constructor
    scanner - reference to TScanner object
}
constructor TScannerIterator.Create(scanner: TScanner);
begin
  inherited Create;
  FScanner := scanner;
  Reset;
end;

{
  Return current token - may cause Access Violation if .Next hasn't been called
  yet (FCurrent will be -1 then)
}
function TScannerIterator.Current: TToken;
begin
  Result := FScanner.Token[FCurrent];
end;

{
  Return boolean indicating whether there are more tokens in TScanner
}
function TScannerIterator.HasMore: Boolean;
begin
  Result := FCurrent < (FScanner.Count-1);
end;

{
  Point to next token.
}
procedure TScannerIterator.NextToken;
begin
  Inc(FCurrent);
end;

{
  Reset iterator.
}
procedure TScannerIterator.Reset;
begin
  FCurrent := -1;
end;

end.
