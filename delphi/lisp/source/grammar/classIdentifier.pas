unit classIdentifier;

interface

uses
  classAtom;

type
  TIdentifier = class(TAtom)
  private
    FValue: string;
  public
    constructor Create(value: string);
    procedure Print(indent: string); override;
    property Value: string read FValue;
  end;

implementation

{ TIdentifier }

constructor TIdentifier.Create(value: string);
begin
  inherited Create;
  FValue := value;
end;

procedure TIdentifier.Print(indent: string);
begin
  WriteLn(indent + 'Identifier: ' + FValue);
end;

end.
