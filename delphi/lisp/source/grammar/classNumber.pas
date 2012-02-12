unit classNumber;

interface

uses
  classAtom;

type
  TNumber = class(TAtom)
  private
    FValue: Integer;
  public
    constructor Create(value: string);
    procedure Print(indent: string); override;
    property Value: Integer read FValue;
  end;

implementation

uses
  SysUtils;

{ TNumber }

constructor TNumber.Create(value: string);
begin
  inherited Create;
  FValue := StrToInt(value);
end;

procedure TNumber.Print(indent: string);
begin
  WriteLn(indent + 'Number: ' + IntToStr(FValue));
end;

end.
