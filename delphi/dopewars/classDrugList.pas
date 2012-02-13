unit classDrugList;
// a collection of drugs

interface

uses
  Classes, Contnrs,
  classDrug;

type
  TSortColumn = (stAmount, stName, stPrice, stDemand);

type
  TDrugList = class(TObject)
  protected
    FDrux: TObjectList;
    FSortCol: TSortColumn;
    FSortInv: Boolean;
    function GetDrug(i:integer): TDrug;
    function GetDrugCount: integer;
  public
    constructor Create;
    destructor Destroy; override;
    function FindDrug(name:string): TDrug;
    function DrugExists(name:string): boolean;
    procedure Sort(col:TSortColumn);
    procedure AddDrug(ADrug:TDrug);
    procedure EmptyList;
    procedure RemoveDrug(ADrug:TDrug);
    property Drugs[i:integer]: TDrug read GetDrug; default;
    property Count: integer read GetDrugCount;
  end;

implementation

constructor TDrugList.Create;
begin
  inherited Create;
  FDrux := TObjectList.Create(False);
  FSortCol := stDemand;
  FSortInv := False;
end;

destructor TDrugList.Destroy;
begin
  FDrux.OwnsObjects := True;
  FDrux.Free;
  inherited Destroy;
end;

function TDrugList.FindDrug(name:string): TDrug;
var
  i: integer;
  drug: TDrug;
begin
  i := 0;
  result := nil;
  while (i < FDrux.Count) and (result = nil) do
  begin
    drug := TDrug(FDrux.Items[i]);
    if drug.Name = name then
      result := drug
    else
      Inc(i);
  end;
end;

function TDrugList.DrugExists(name:string): boolean;
begin
  result := FindDrug(name) <> nil;
end;

procedure TDrugList.Sort(col:TSortColumn);
          function InOrder(col:TSortColumn; d1,d2:TDrug): boolean;
          begin
            result := True;
            if FSortInv then
              case col of
                stName:   result := d1.Name >= d2.Name;
                stPrice:  result := d1.EffectivePrice >= d2.EffectivePrice;
                stDemand: result := d1.Demand >= d2.Demand;
                stAmount: result := d1.Amount >= d2.Amount;
              end
            else
              case col of
                stName:   result := d1.Name <= d2.Name;
                stPrice:  result := d1.EffectivePrice <= d2.EffectivePrice;
                stDemand: result := d1.Demand <= d2.Demand;
                stAmount: result := d1.Amount <= d2.Amount;
              end;
          end;
var
  i: integer;
  d1,d2: TDrug;
begin
  FSortInv := (FSortCol = col) and not FSortInv;
  FSortCol := col;
  i := 0;
  while i < (FDrux.Count - 1) do
  begin
    d1 := TDrug(FDrux.Items[i]);
    d2 := TDrug(FDrux.Items[i+1]);
    if InOrder(col,d1,d2) then
      i := i + 1
    else
    begin
      FDrux.Exchange(i,i+1);
      if i = 0 then
        i := 1
      else
        i := i - 1;
    end;
  end;
end;

function TDruglist.GetDrug(i:integer): TDrug;
begin
  result := nil;
  if (i >= 0) and (i < FDrux.Count) then
    result := TDrug(FDrux.Items[i]);
end;

function TDrugList.GetDrugCount: integer;
begin
  result := FDrux.Count;
end;

procedure TDrugList.AddDrug(ADrug:TDrug);
begin
  FDrux.Add(ADrug);
end;

procedure TDrugList.EmptyList;
begin
  FDrux.Clear;
end;

procedure TDrugList.RemoveDrug(ADrug:TDrug);
begin
  FDrux.Remove(ADrug);
end;

end.
