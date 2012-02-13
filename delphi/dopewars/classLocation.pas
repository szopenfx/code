unit classLocation;
// braindead class for a location

interface

type
  TLocation = class(TObject)
  private
    FName: string;
    FActions: integer;
  public
    constructor Create(s:string; i:integer); overload;
    constructor Create(fromstring:string); overload;
    function AsString: string;
    procedure SetFromString(s:string);
    property Name: string read FName;
    property Actions: integer read FActions;
  end;

implementation

uses
  SysUtils;

constructor TLocation.Create(s:string; i:integer);
begin
  inherited Create;
  FName := s;
  FActions := i;
end;

constructor TLocation.Create(fromstring:string);
begin
  inherited Create;
  SetFromString(fromstring);
end;

function TLocation.AsString: string;
begin
  result := FName + #7 + IntToStr(FActions);
end;

procedure TLocation.SetFromString(s:string);
var
  beeppos: integer;
begin
  beeppos := Pos(#7,s);
  FName := Copy(s,1,beeppos - 1);
  FActions := StrToInt(Copy(s,beeppos + 1,Length(s) - beeppos));
end;

end.
