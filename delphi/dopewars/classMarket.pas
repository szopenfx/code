unit classMarket;
// a DrugList that contains all available drux in a location

interface

uses
  ComCtrls,
  classDrugList, classReplicator;

type
  TMarket = class(TDrugList)
  public
    procedure SetDrugMenu;
    constructor Create;
    destructor Destroy; override;
    procedure ShowInListView(marketLV:TListView);
  end;

implementation

uses
  SysUtils,
  classDrug, classDopeWars;

constructor TMarket.Create;
begin
  inherited Create;
  SetDrugMenu;
end;

destructor TMarket.Destroy;
begin
  inherited Destroy;
end;

procedure TMarket.ShowInListView(marketLV:TListView);
var
  listitem: TListItem;
  i: integer;
begin
  marketLV.Visible := False;
  marketLV.Items.Clear;
  for i := 0 to Count - 1 do
  begin
    listitem := marketLV.Items.Add;
    listitem.Caption := Drugs[i].Name;
    listitem.Data := Drugs[i];
    listitem.SubItems.Add(IntToStr(Drugs[i].EffectivePrice) + ',-');
    if dope.Cheats.Demand then
      listitem.SubItems.Add(IntToStr(Drugs[i].Demand) + '%');
  end;
  marketLV.Visible := True;
end;

procedure TMarket.SetDrugMenu;
var
  i: integer;
begin
  EmptyList;
  for i := 0 to dope.Replicator.Count - 1 do
    if Random(100) < 80 then
    begin
      AddDrug(TDrug.Create(dope.Replicator[i]));
      Drugs[Count - 1].RandomDemand;
    end;
end;

end.
