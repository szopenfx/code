unit formBrothel;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls;

type
  TfrmBrothel = class(TForm)
    lblPimp: TLabel;
    lblBitchBuy: TLabel;
    lblBitchSell: TLabel;
    btnBuyBitch: TButton;
    btnSellBitch: TButton;
    btnViolence: TButton;
    btnClose: TButton;
    lblHave: TLabel;
    procedure btnBuyBitchClick(Sender: TObject);
    procedure btnSellBitchClick(Sender: TObject);
    procedure btnViolenceClick(Sender: TObject);
    procedure btnCloseClick(Sender: TObject);
  public
    procedure UpdateForm;
    procedure ShowForm;
  end;

var
  frmBrothel: TfrmBrothel;

implementation

uses
  globals, classDopeWars, formAmount, classPerson, formViolence;

{$R *.dfm}

procedure TfrmBrothel.UpdateForm;
begin
  with dope.People do
  begin
    lblPimp.Caption := Format(sBrothelPimpName,[Pimp.Name]);
    lblBitchBuy.Caption := Format(sBrothelBuy,[Pimp.SellsAt]);
    lblBitchSell.Caption := Format(sBrothelSell,[Pimp.BuysAt]);
    lblHave.Caption := Format(sBrothelHave,[Player.Bitches,Player.Jacket.Size]);
  end;
  dope.UpdateForm;
end;

procedure TfrmBrothel.ShowForm;
begin
  UpdateForm;
  Show;
end;

procedure TfrmBrothel.btnBuyBitchClick(Sender: TObject);
var
  max,amount: integer;
begin
  with dope.People do
    max := Player.Wallet.Cash div Pimp.SellsAt;
  amount := frmAmount.ShowForm(sBuyBitch,'',max,dope.People.Pimp.SellsAt);
  if amount > 0 then
    with dope.People do
      Pimp.SellBitch(Player,amount);
  UpdateForm;
end;

procedure TfrmBrothel.btnSellBitchClick(Sender: TObject);
var
  max,amount: integer;
begin
  max := dope.People.Player.Bitches;
  amount := frmAmount.ShowForm(sSellBitch,'',max,dope.People.Pimp.BuysAt);
  if amount > 0 then
    dope.People.Pimp.SellBitch(dope.People.Player,amount);
  UpdateForm;
end;

procedure TfrmBrothel.btnViolenceClick(Sender: TObject);
begin
  dope.Violence.Reset;
  dope.Violence.AddPerson(dope.People.Pimp);
  frmViolence.ShowForm;
end;

procedure TfrmBrothel.btnCloseClick(Sender: TObject);
begin
  Close;
end;

end.
