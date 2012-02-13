unit formBank;
// the bank dialog

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls;

type
  TfrmBank = class(TForm)
    Label1: TLabel;
    Label2: TLabel;
    btnWithdraw: TButton;
    btnDeposit: TButton;
    Button1: TButton;
    btnClose: TButton;
    procedure btnWithdrawClick(Sender: TObject);
    procedure btnDepositClick(Sender: TObject);
    procedure btnCloseClick(Sender: TObject);
  public
    procedure UpdateForm;
    procedure ShowForm;
  end;

var
  frmBank: TfrmBank;

implementation

uses
  formMain, classDopeWars, formAmount, globals;

{$R *.dfm}

procedure TfrmBank.btnWithdrawClick(Sender: TObject);
const
  Swithdraw = 'You can withdraw ƒ %d.';
var
  max,amount: integer;
begin
  with dope.People.Player.BankAccount do
    max := Balance - DebtLimit;
  if max > 0 then
  begin
    amount := frmAmount.ShowForm(sBankWithdraw,'',max,1);
    if amount > -1 then
    begin
      dope.People.Player.BankAccount.Withdraw(amount);
      dope.People.Player.Wallet.Earn(amount);
    end;
  end
  else
    dope.Messenger.AddMessage(sBankWithdrawDebtLimit);
  UpdateForm;
end;

procedure TfrmBank.btnDepositClick(Sender: TObject);
var
  max,amount: integer;
begin
  max := dope.People.Player.Wallet.Cash;
  if max > 0 then
  begin
    amount := frmAmount.ShowForm(sBankDeposit,'',max,1);
    if amount > -1 then
    begin
      dope.People.Player.Wallet.Spend(amount);
      dope.People.Player.BankAccount.Deposit(amount);
    end;
  end
  else
    dope.Messenger.AddMessage(sBankDepositNoMoney);
  UpdateForm;
end;

procedure TfrmBank.UpdateForm;
begin
  with dope.People.Player.BankAccount do begin
    Label1.Caption := Format(sBankLblCredit,[Balance]);
    Label2.Caption := Format(sBankLblInterest,[Interest]);
  end;
  Main.HandleUpdate;
end;

procedure TfrmBank.ShowForm;
begin
  main.Enabled := False;
  Visible := True;
  UpdateForm;
end;

procedure TfrmBank.btnCloseClick(Sender: TObject);
begin
  main.Enabled := True;
  Close;
end;

end.
