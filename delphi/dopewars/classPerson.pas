unit classPerson;
// class representing a person

interface

uses
  classWallet, classHolster, classBankAccount, classJacket;

type
  TPerson = class(TObject)
  protected
    FName: string;
    FHealth: integer;
    FBitches: integer;
    FWallet: TWallet;
    FHolster: THolster;
    FBankAccount: TBankAccount;
    FJacket: TJacket;
    FKidneys: integer;
  public
    constructor Create(AName:string; ACash:integer; ABankAccount:TBankAccount);
    destructor Destroy; override;
    procedure GetShot(AWeapon:TObject);
    function IsDead: boolean;
    function IsAlive: boolean;
    procedure GetHealed(hp:integer);
    function RemoveKidney(amount,cash:integer): boolean;
    procedure AddKidney(amount:integer);
    procedure MedicalUpdate;
    property Name: string read FName write FName;
    property Health: integer read FHealth;
    property Bitches: integer read FBitches write FBitches;
    property Wallet: TWallet read FWallet;
    property Holster: THolster read FHolster;
    property BankAccount: TBankAccount read FBankAccount;
    property Jacket: TJacket read FJacket;
    property Kidneys: integer read FKidneys;
  end;

implementation

uses
  Dialogs, SysUtils, Forms,
  classDopeWars, classWeapon, globals;

constructor TPerson.Create(AName:string; ACash:integer; ABankAccount:TBankAccount);
begin
  inherited Create;
  FName := AName;
  FHealth := 100;
  FBitches := 0;
  FWallet := TWallet.Create(ACash);
  FHolster := THolster.Create(Self);
  FBankAccount := ABankAccount;
  FJacket := TJacket.Create(Self);
  FKidneys := 2;
end;

destructor TPerson.Destroy;
begin
  FJacket.Free;
  FBankAccount.Free;
  FHolster.Free;
  FWallet.Free;
  inherited Destroy;
end;

procedure TPerson.GetShot(AWeapon:TObject);
begin
  FHealth := FHealth - TWeapon(AWeapon).Damage;
end;

function TPerson.IsDead: boolean;
begin
  result := FHealth <= 0;
end;

procedure TPerson.GetHealed(hp:integer);
begin
  if hp > 0 then
    FHealth := FHealth + hp;
  if FHealth > 100 then
    FHealth := 100;
end;

function TPerson.IsAlive: boolean;
begin
  result := FHealth > 0;
end;

function TPerson.RemoveKidney(amount,cash:integer): boolean;
begin
  result := (FKidneys >= amount) and (amount > 0);
  if result then
  begin
    FWallet.Earn(cash);
    FKidneys := FKidneys - amount;
  end;
end;

procedure TPerson.AddKidney(amount:integer);
begin
  FKidneys := FKidneys + amount;
end;

procedure TPerson.MedicalUpdate;
var
  wasdead: boolean;
begin
  if FKidneys = 0 then
  begin
    wasdead := IsDead;
    FHealth := FHealth - 50;
    if IsDead and not wasdead then
      dope.Messenger.PopupMessage(sKidneyDeath);
  end;
end;

end.
