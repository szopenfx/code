unit classDoctor;
// class for the doctor-person

interface

uses
  classPerson;

type
  TDoctor = class(TPerson)
  private
    FCost: integer;
    FKidneyBuy: integer;
    FKidneySell: integer;
  public
    constructor Create(cost:integer);
    procedure Heal(hp:integer; thePerson:TPerson);
    procedure BuyKidney(thePerson:TPerson);
    procedure SellKidney(thePerson:TPerson);
    property Cost: integer read FCost;
    property KidneyBuy: integer read FKidneyBuy;
    property KidneySell: integer read FKidneySell;
  end;

implementation

uses
  SysUtils,
  classBankAccount, classDopeWars, formAmount, globals;

constructor TDoctor.Create(cost:integer);
var
  ABankAccount: TBankAccount;
begin
  ABankAccount := TBankAccount.Create(Random(8000),3,-2000);
  inherited Create('Doctor',Random(6000),ABankAccount);
  Jacket.AddDrug(dope.Replicator.Duplicate('Ludes',100));
  Jacket[0].RandomDemand;
  FCost := cost;
  FKidneyBuy := Random(500) + 500;
  FKidneySell := Random(2000) + 9000;
end;

procedure TDoctor.Heal(hp:integer; thePerson:TPerson);
begin
  if thePerson.Wallet.Spend(hp * FCost) then
  begin
    FWallet.Earn(hp * FCost);
    thePerson.GetHealed(hp);
  end;
end;

procedure TDoctor.BuyKidney(thePerson:TPerson);
var
  amount: integer;
begin
  amount := frmAmount.ShowForm(sSellKidney,'',thePerson.Kidneys,FKidneyBuy);
  if amount > 0 then
  begin
    if FWallet.Spend(FKidneyBuy * amount) then
      thePerson.RemoveKidney(amount,FKidneyBuy * amount)
    else
      dope.Messenger.PopupMessage(sDoctorBroke);
  end;
end;

procedure TDoctor.SellKidney(thePerson:TPerson);
var
  cap,msg: string;
  amount: integer;
begin
  cap := sBuyKidney.cap;
end;

end.
