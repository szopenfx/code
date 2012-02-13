unit classDrug;
// class for one dose of drug

interface

type
  TDrug = class(TObject)
  protected
    FName: string;
    FPrice: integer;
    FDemand: integer;
    FAmount: integer;
  public
    constructor Create(n:string; pr:integer; dm:integer=100; am:integer=1); overload;
    constructor Create(dr:TDrug); overload;
    constructor Create(recstring:string); overload;
    destructor Destroy; override;
    procedure SetFromString(recstring:string);
    function GetAsString: string;
    procedure CopyFrom(dr:TDrug);
    procedure CopyTo(dr:TDrug);
    procedure RandomDemand;
    procedure ZeroDemand;
    procedure Abundance;
    procedure Shortage;
    function EffectivePrice: integer;
    procedure Add(ADrug:TDrug);
    procedure Substract(ADrug:TDrug);
    property Name: string read FName;
    property Price: integer read FPrice;
    property Demand: integer read FDemand;
    property Amount: integer read FAmount write FAmount;
  end;

implementation

uses
  SysUtils;

constructor TDrug.Create(n:string; pr:integer; dm:integer=100; am:integer=1);
begin
  inherited Create;
  FName := n;
  FPrice := pr;
  FDemand := dm;
  FAmount := am;
end;

constructor TDrug.Create(dr:TDrug);
begin
  inherited Create;
  CopyFrom(dr);
end;

destructor TDrug.Destroy;
begin
  inherited Destroy;
end;

procedure TDrug.CopyFrom(dr:TDrug);
begin
  FName := dr.Name;
  FPrice := dr.Price;
  FDemand := dr.Demand;
  FAmount := dr.Amount;
end;

procedure TDrug.CopyTo(dr:TDrug);
begin
  dr.CopyFrom(Self);
end;

procedure TDrug.RandomDemand;
begin
  FDemand := 75 + Random(51);
end;

procedure TDrug.ZeroDemand;
begin
  FDemand := 0;
end;

procedure TDrug.Abundance;
begin
  FDemand := 10 + Random(30);
end;

procedure TDrug.Shortage;
begin
  FDemand := 150 + Random(300);
end;

function TDrug.EffectivePrice;
begin
  result := Trunc(FPrice * (FDemand / 100));
end;

procedure TDrug.Add(ADrug:TDrug);
begin
  FDemand := ((FDemand * FAmount) + (ADrug.Demand * ADrug.Amount))
               div (ADrug.Amount + FAmount);
  FAmount := ADrug.Amount + FAmount;
end;

procedure TDrug.Substract(ADrug:TDrug);
begin
  FAmount := FAmount - ADrug.Amount;
end;

function TDrug.GetAsString: string;
begin
  result := Format('%s@%d',[FName,FPrice]);
end;

constructor TDrug.Create(recstring:string);
begin
  inherited Create;
  SetFromString(recstring);
end;

procedure TDrug.SetFromString(recstring:string);
var
  X: integer;
begin
  X := Pos(recstring,'@');
  FName := Copy(recstring,1,X-1);
  FAmount := Length(Copy(recstring,X+1,Length(recstring)-X));
end;

end.
