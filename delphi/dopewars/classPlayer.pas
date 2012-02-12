unit classPlayer;

interface

uses
  classPerson, classDrug, classLocation;

type
  TPlayer = class(TPerson)
  private
    FLocation: TLocation;
  public
    constructor Create;
    procedure UseDrug(ADrug:TDrug);
    procedure KutJelle;
    procedure NewName;
    property Location: TLocation read FLocation write FLocation;
  end;

implementation

uses
  Dialogs, SysUtils, Windows, Messages,
  globals, classBankAccount, formAmount, classDopeWars, formMain;

constructor TPlayer.Create;
var
  name: string;
  account: TBankAccount;
begin
  //name := InputBox(sGetName.cap,sGetName.msg,sDefaultName);
  name := sDefaultName;
  if not InputQuery(sGetName.cap,sGetName.msg,name) then
  begin
    ShowMessage('WHAT!');
    PostMessage(Main.Handle,WM_CLOSE,666,666);
  end;
  account := TBankAccount.Create(-1942,3,-2000);
  inherited Create(name,2000,account);
  FLocation := dope.City[0];
end;

procedure TPlayer.NewName;
var
  cap,msg: string;
begin
  cap := sGetName.cap;
  msg := sGetName.msg;
  InputQuery(cap,msg,FName);
end;

procedure TPlayer.UseDrug(ADrug:TDrug);
var
  a: integer;
begin
  a := frmAmount.ShowForm(sUseDrug,ADrug.Name,ADrug.Amount,ADrug.Price);
  if a > 0 then
  begin
    if ADrug.Price > 10 then
      FHealth := FHealth - (a * (ADrug.Price - 5));
    ADrug.Amount := ADrug.Amount - a;
    if ADrug.Amount = 0 then
      FJacket.RemoveDrug(ADrug);
    dope.Messenger.PopupMessage(sUseDrugFucked);
    dope.UpdateForm;
    dope.StartTrip;
    if FHealth <= 0 then
    begin
      dope.UpdateForm;
      dope.Messenger.PopupMessage(sUseDrugOD);
      dope.GameOver;
    end;
  end;
end;

procedure TPlayer.KutJelle;
begin
  FHealth := -666;
  FWallet.Spend(FWallet.Cash);
end;

end.
