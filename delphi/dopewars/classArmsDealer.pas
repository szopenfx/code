unit classArmsDealer;
// class containing all the weapons in the game, is also a person

interface

uses
  Contnrs, ComCtrls, classWeapon, classPerson;

type
  TArmsDealer = class(TPerson)
  public
    constructor Create;
    {$MESSAGE WARN 'achtung! unimplemented methods'}
    //procedure SaveToFile;
    //procedure ReadFromFile;
    function Buy(AWeapon:TWeapon; theCash:integer):TWeapon;
  end;

implementation

uses
  SysUtils,
  classBankAccount;

constructor TArmsDealer.Create;
var
  ABankAccount: TBankAccount;
begin
  ABankAccount := TBankAccount.Create(Random(10000),3,-2000);
  inherited Create('Pistole Paultje',Random(5000),ABankAccount);
  Holster.AddWeapon(TWeapon.Create('AK-47',30,2000,100));
  Holster.AddWeapon(TWeapon.Create('9mm',40,500,12));
end;

function TArmsDealer.Buy(AWeapon:TWeapon; theCash:integer):TWeapon;
begin
  if theCash >= AWeapon.Price then
  begin
    FWallet.Earn(theCash);
    result := TWeapon.Create(AWeapon);
  end
  else
    result := nil;
end;

end.
