unit classHolster;
// class for holding the player's weapons

interface

uses
  Contnrs, ComCtrls,
  classWeapon;

type
  THolster = class(TObject)
  private
    FOwner: TObject;
    FWeapons: TObjectList;
    function GetWeapon(i:integer): TWeapon;
    function GetWeaponCount: integer;
  public
    constructor Create(owner:TObject);
    destructor Destroy; override;
    procedure AddWeapon(AWeapon:TWeapon);
    procedure BuyWeapon(AWeapon:TWeapon);
    procedure SellWeapon(AWeapon:TWeapon);
    procedure RemoveWeapon(AWeapon:TWeapon);
    procedure ShowInListView(AListView:TListView);
    property Weapons[i:integer]: TWeapon read GetWeapon; DEFAULT;
    property Count: integer read GetWeaponCount;
  end;

implementation

uses
  SysUtils,
  classDopeWars, globals, classPerson;

constructor THolster.Create(owner:TObject);
begin
  inherited Create;
  FWeapons := TObjectList.Create(False);
  FOwner := owner;
end;

destructor THolster.Destroy;
begin
  FWeapons.OwnsObjects := True;
  FWeapons.Free;
  inherited Destroy;
end;

procedure THolster.AddWeapon(AWeapon:TWeapon);
begin
  FWeapons.Add(AWeapon);
end;

procedure THolster.BuyWeapon(AWeapon:TWeapon);
begin
  if AWeapon.Price <= TPerson(FOwner).Wallet.Cash then
  begin
    TPerson(FOwner).Wallet.Spend(AWeapon.Price);
    FWeapons.Add(dope.People.ArmsDealer.Buy(AWeapon,AWeapon.Price));
    dope.Messenger.AddMessage(Format(sBuyWeaponBought,[AWeapon.Name]));
  end
  else
    dope.Messenger.AddMessage(Format(sBuyWeaponNoMoney,[AWeapon.Name]));
end;

procedure THolster.SellWeapon(AWeapon:TWeapon);
begin

end;

function THolster.GetWeaponCount: integer;
begin
  result := FWeapons.Count;
end;

function THolster.GetWeapon(i:integer): TWeapon;
begin
  result := nil;
  if (i >= 0) and (i < FWeapons.Count) then
    result := TWeapon(FWeapons.Items[i])
end;

procedure THolster.RemoveWeapon(AWeapon:TWeapon);
begin
  if FWeapons.IndexOf(AWeapon) > -1 then
    FWeapons.Remove(AWeapon);
end;

procedure THolster.ShowInListView(AListView:TListView);
var
  i: integer;
begin
  AListView.Items.Clear;
  for i := 0 to Count - 1 do
    with AListView.Items.Add do
    begin
      Caption := Weapons[i].Name;
      SubItems.Add(IntToStr(Weapons[i].Price));
      SubItems.Add(IntToStr(Weapons[i].Damage));
      SubItems.Add(IntToStr(Weapons[i].Ammo));
      Data := Weapons[i];
    end;
end;



end.
