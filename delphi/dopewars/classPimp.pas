unit classPimp;

interface

uses
  classPerson;

type
  TPimp = class(TPerson)
  private
    FBuysAt: integer;
    FSellsAt: integer;
  public
    constructor Create;
    procedure BuyBitch(fromPerson:TPerson; amount:integer);
    procedure SellBitch(toPerson:TPerson; amount:integer);
    property BuysAt: integer read FBuysAt;
    property SellsAt: integer read FSellsAt;
  end;


implementation

uses
  classBankAccount, classDopeWars;

constructor TPimp.Create;
var
  account: TBankAccount;
begin
  account := TBankAccount.Create(Random(5000),3,-2000);
  inherited Create('Pimp L',Random(10000),account);
  FJacket.AddDrug(dope.Replicator.Duplicate('Heroin',Random(30)));
  FJacket.AddDrug(dope.Replicator.Duplicate('Cocaine',Random(20)));
  FBuysAt := 500;
  FSellsAt := 1000;
end;

procedure TPimp.BuyBitch(fromPerson:TPerson; amount:integer);
begin
  if Wallet.Spend(FBuysAt * amount) then
  begin
    fromPerson.Wallet.Earn(FBuysAt * amount);
    fromPerson.Jacket.RemoveBitch(amount);
  end;
end;

procedure TPimp.SellBitch(toPerson:TPerson; amount:integer);
begin
  if toPerson.Wallet.Spend(FSellsAt * amount) then
  begin
    Wallet.Earn(FSellsat * amount);
    toPerson.Jacket.AddBitch(amount);
  end;
end;

end.
