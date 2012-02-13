unit classWeapon;
// class for a weapon

interface

type
  TWeapon = class(TObject)
  protected
    FName: string;
    FAmmo: integer;
    FDamage: integer;
    FPrice: integer;
  public
    constructor Create(nm:string; dm,pr,am:integer); overload;
    constructor Create(AWeapon:TWeapon); overload;
    procedure Shoot(APerson:TObject);
    property Name: string read FName;
    property Ammo: integer read FAmmo;
    property Damage: integer read FDamage;
    property Price: integer read FPrice;
  end;

implementation

uses
  classPerson;

constructor TWeapon.Create(nm:string; dm,pr,am:integer);
begin
  inherited Create;
  FName := nm;
  FDamage := dm;
  FPrice := pr;
  FAmmo := am;
end;

constructor TWeapon.Create(AWeapon:TWeapon);
begin
  inherited Create;
  FName := AWeapon.Name;
  FAmmo := AWeapon.Ammo;
  FDamage := AWeapon.Damage;
  FPrice := AWeapon.Price;
end;

procedure TWeapon.Shoot(APerson:TObject);
begin
  TPerson(APerson).GetShot(Self);
  Dec(FAmmo);
end;

end.
