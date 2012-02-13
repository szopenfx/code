unit formViolence;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, Gauges;

type
  TfrmViolence = class(TForm)
    gbPlayer: TGroupBox;
    gbGroup: TGroupBox;
    gPlayerHP: TGauge;
    cmbWeapons: TComboBox;
    btnAttack: TButton;
    Label1: TLabel;
    gGroupHP: TGauge;
    btnMoney: TButton;
    btnWeapons: TButton;
    btnDrugs: TButton;
    procedure cmbWeaponsChange(Sender: TObject);
    procedure btnAttackClick(Sender: TObject);
    procedure btnMoneyClick(Sender: TObject);
    procedure btnDrugsClick(Sender: TObject);
    procedure btnWeaponsClick(Sender: TObject);
  public
    procedure ShowForm;
    procedure UpdateForm;
  end;

var
  frmViolence: TfrmViolence;

implementation

uses
  classWeapon, classDrug, globals, classDrugList, classHolster, classDopeWars;

{$R *.dfm}

procedure TfrmViolence.ShowForm;
var
  i: integer;
begin
  if dope.People.Player.Holster.Count > 0 then
  begin
    cmbWeapons.Items.Clear;
    cmbWeapons.Enabled := True;
    for i := 0 to dope.People.Player.Holster.Count - 1 do
      cmbWeapons.Items.Add(dope.People.Player.Holster[i].Name);
  end
  else
    cmbWeapons.Enabled := False;
  UpdateForm;
  Show;
end;

procedure TfrmViolence.UpdateForm;
begin
  gbPlayer.Caption := dope.People.Player.Name;
  gPlayerHP.Progress := dope.People.Player.Health;
  gbGroup.Caption := dope.Violence.Group[0].Name;
  gGroupHP.Progress := dope.Violence.Group[0].Health;
  if cmbWeapons.ItemIndex > -1 then
    with dope.People.Player.Holster[cmbWeapons.ItemIndex] do
      Label1.Caption := Format(sViolenceWeapon,[Name,Ammo])
  else
    Label1.Caption := sViolenceNoWeapon;
  btnAttack.Enabled := not dope.Violence.Group[0].IsDead;
  btnMoney.Enabled := dope.Violence.Group[0].IsDead;
  btnDrugs.Enabled := dope.Violence.Group[0].IsDead;
  btnWeapons.Enabled := dope.Violence.Group[0].IsDead;
end;

procedure TfrmViolence.cmbWeaponsChange(Sender: TObject);
begin
  UpdateForm;
end;

procedure TfrmViolence.btnAttackClick(Sender: TObject);
var
  i: integer;
begin
  i := cmbWeapons.ItemIndex;
  if i > -1 then
    dope.Violence.Fight(i);
  UpdateForm;
  dope.UpdateForm;
end;

procedure TfrmViolence.btnMoneyClick(Sender: TObject);
var
  x: integer;
begin
  x := dope.Violence.Group[0].Wallet.Cash;
  dope.People.Player.Wallet.Earn(x);
  dope.Violence.Group[0].Wallet.Spend(x);
  dope.UpdateForm;
end;

procedure TfrmViolence.btnDrugsClick(Sender: TObject);
var
  i: integer;
  theDrug: TDrug;
begin
  for i := dope.Violence.Group[0].Jacket.Count - 1 downto 0 do
  begin
    theDrug := dope.Violence.Group[0].Jacket[i];
    theDrug.ZeroDemand;
    if dope.People.Player.Jacket.DrugExists(theDrug.Name)
      then dope.People.Player.Jacket.FindDrug(theDrug.Name).Add(theDrug)
      else dope.People.Player.Jacket.AddDrug(theDrug);
    dope.Violence.Group[0].Jacket.RemoveDrug(theDrug);
  end;
  dope.UpdateForm;
end;

procedure TfrmViolence.btnWeaponsClick(Sender: TObject);
var
  i: integer;
begin
  with dope.People, dope.Violence do
  for i := Group[0].Holster.Count - 1 downto 0 do
  begin
    Player.Holster.AddWeapon(Group[0].Holster[i]);
    Group[0].Holster.RemoveWeapon(Group[0].Holster[i]);
  end;
  dope.UpdateForm;
end;

end.
