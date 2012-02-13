unit classScore;
// class for an entry in the high score list

interface

type
  TScore = class(TObject)
  private
    FName: string;
    FCash: integer;
    FHealth: integer;
  public
    constructor Create(name:string; cash,health:integer); overload;
    constructor Create(from_string:string); overload;
    destructor Destroy; override;
    function ToString: string;
    procedure FromString(s:string);
    property Name: string read FName write FName;
    property Cash: integer read FCash;
    property Health: integer read FHealth;
  end;

implementation

uses
  SysUtils, StrUtils;

constructor TScore.Create(name:string; cash,health:integer);
begin
  inherited Create;
  FName := name;
  FCash := cash;
  FHealth := health;
end;

constructor TScore.Create(from_string:string);
begin
  inherited Create;
  FromString(from_string);
end;

destructor TScore.Destroy;
begin
  inherited Destroy;
end;

function TScore.ToString: string;
var
  i: integer;
begin
  result := FName + #7 + IntToStr(FCash) + #7 + IntToStr(FHealth);
end;

procedure TScore.FromString(s:string);
const
  ss: string = #7;
var
  a,b: integer;
begin
  a := PosEx(ss,s,1) + 1;
  b := PosEx(ss,s,a) + 1;
  FName := Copy(s, 1, a - 2);
  FCash := StrToInt(Copy(s,a,b - a - 1));
  FHealth := StrToInt(Copy(s,b,Length(s) - b + 1));
end;

end.
