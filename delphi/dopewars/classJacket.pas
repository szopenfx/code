unit classJacket;
// DrugList that contains everything in the player's jacket

interface

uses
  ComCtrls, CheckLst, SysUtils,
  classDrugList, classDrug;

type
  TJacket = class(TDrugList)
  private
    FSize: integer;
    FOwner: TObject;
    function SpaceOccupied: integer;
  public
    constructor Create(owner:TObject);
    procedure ShowInListView(AListView:TListView);
    function BuyDrug(theDrug:TDrug): integer;
    function SellDrug(theDrug:TDrug): integer;
    procedure AddBitch(amount:integer=1);
    procedure RemoveBitch(amount:integer=1);
    property Size: integer read FSize;
    property Used: integer read SpaceOccupied;
  end;

implementation

uses
  Dialogs, globals,
  formAmount, classDopeWars, classPerson;

constructor TJacket.Create(owner:TObject);
begin
  inherited Create;
  FOwner := owner;
  FSize := 100;
end;

procedure TJacket.ShowInListView(AListView:TListView);
var
  i: integer;
  lvi: TListItem;
begin
  AListView.Items.Clear;
  for i := 0 to Count - 1 do
  begin
    lvi := AListView.Items.Add;
    lvi.Caption := IntToStr(Drugs[i].Amount);
    lvi.Data := Drugs[i];
    lvi.SubItems.Add(Drugs[i].Name);
    lvi.SubItems.Add(IntToStr(Drugs[i].EffectivePrice) + ',-')
  end;
end;

function TJacket.BuyDrug(theDrug:TDrug): integer;
var
  max,maxMoney,maxSpace: integer;
begin
  result := 0;
  maxMoney := dope.People.Player.Wallet.Cash div theDrug.EffectivePrice;
  maxSpace := FSize - SpaceOccupied;
  if maxMoney < maxSpace
    then max := maxMoney
    else max := maxSpace;
  if max < 0
    then max := 0;
  theDrug.Amount := frmAmount.ShowForm(sBuyDrug,theDrug.Name,max,theDrug.EffectivePrice);
  if theDrug.Amount > 0 then
  begin
    result := theDrug.EffectivePrice * theDrug.Amount;
    if DrugExists(theDrug.Name)
      then FindDrug(theDrug.Name).Add(theDrug)
      else AddDrug(TDrug.Create(theDrug));
  end;
end;

function TJacket.SellDrug(theDrug:TDrug):integer;
var
  marketDrug: TDrug;
  amount:integer;
begin
  result := 0;
  marketDrug := dope.Market.FindDrug(theDrug.Name);
  if marketdrug <> nil then
  begin
    amount := frmAmount.ShowForm(sSellDrug,theDrug.Name,theDrug.Amount,marketdrug.EffectivePrice);
    if amount > -1 then
    begin
      result := amount * marketdrug.EffectivePrice;
      theDrug.Amount := theDrug.Amount - amount;
      if theDrug.Amount = 0 then
        RemoveDrug(theDrug);
    end
  end
  else
    dope.Messenger.AddMessage(Format(sNoDemand,[theDrug.Name]));
end;

function TJacket.SpaceOccupied: integer;
var
  i: integer;
begin
  result := 0;
  for i := 0 to Count - 1 do
    result := result + Drugs[i].Amount;
end;

procedure TJacket.AddBitch(amount:integer=1);
var
  i: integer;
begin
  for i := 1 to amount do
  begin
    Inc(FSize,50);
    TPerson(FOwner).Bitches := TPerson(FOwner).Bitches + 1;
  end;
end;

procedure TJacket.RemoveBitch(amount:integer=1);
var
  i: integer;
begin
  for i := 1 to amount do
  begin
    Dec(FSize,50);
    TPerson(FOwner).Bitches := TPerson(FOwner).Bitches + 1;
  end;
end;

end.
