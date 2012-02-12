unit formDoctor;
// doctor form

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls;

type
  TfrmDoctor = class(TForm)
    lblHP: TLabel;
    btnHeal: TButton;
    btnViolence: TButton;
    btnClose: TButton;
    lblPrice: TLabel;
    btnSellKidneys: TButton;
    lblKidneyBuy: TLabel;
    lblKidneySell: TLabel;
    btnBuyKidneys: TButton;
    procedure btnHealClick(Sender: TObject);
    procedure btnViolenceClick(Sender: TObject);
    procedure btnCloseClick(Sender: TObject);
    procedure btnSellKidneysClick(Sender: TObject);
    procedure btnBuyKidneysClick(Sender: TObject);
  public
    procedure ShowForm;
  end;

var
  frmDoctor: TfrmDoctor;

implementation

uses
  classDopeWars, globals, formAmount, formViolence;

{$R *.dfm}

procedure TfrmDoctor.ShowForm;
begin
  lblHP.Caption := Format(sDoctorLblHP,[dope.People.Player.Health]);
  lblPrice.Caption := Format(sDoctorLblCost,[dope.People.Doctor.Cost]);
  lblKidneyBuy.Caption := Format(sDoctorKidneyBuy,[dope.People.Doctor.KidneyBuy]);
  lblKidneySell.Caption := Format(sDoctorKidneySell,[dope.People.Doctor.KidneySell]);
  Visible := True;
end;

procedure TfrmDoctor.btnHealClick(Sender: TObject);
var
  amount,max,maxMoney,maxDamage: integer;
begin
  maxMoney := dope.People.Player.Wallet.Cash div dope.People.Doctor.Cost;
  maxDamage := 100 - dope.People.Player.Health;
  if maxMoney < maxDamage
    then max := maxMoney
    else max := maxDamage;
  amount := frmAmount.ShowForm(sDoctorHeal,'',max,dope.People.Doctor.Cost);
  dope.People.Doctor.Heal(amount,dope.People.Player);
  ShowForm;
  dope.UpdateForm;
end;

procedure TfrmDoctor.btnViolenceClick(Sender: TObject);
begin
  dope.Violence.Reset;
  dope.Violence.AddPerson(dope.People.Doctor);
  frmViolence.ShowForm;
end;

procedure TfrmDoctor.btnCloseClick(Sender: TObject);
begin
  Close;
end;

procedure TfrmDoctor.btnSellKidneysClick(Sender: TObject);
begin
  dope.People.Doctor.BuyKidney(dope.People.Player);
  dope.UpdateForm;
end;

procedure TfrmDoctor.btnBuyKidneysClick(Sender: TObject);
begin
//  dope.People.Doctor.SellKidney()
end;

end.
