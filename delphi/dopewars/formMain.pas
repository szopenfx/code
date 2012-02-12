unit formMain;
// main form

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ExtCtrls, ComCtrls, Menus,
  frameMenu;

type
  TMain = class(TForm)
    btnArmsDealer: TButton;
    btnBank: TButton;
    btnBrothel: TButton;
    btnDoctor: TButton;
    btnHighScores: TButton;
    btnNewGame: TButton;
    btnOuterSpace: TButton;
    gbxStats: TGroupBox;
    gbxLocations: TGroupBox;
    gbxActions: TGroupBox;
    lblCash: TLabel;
    lblBank: TLabel;
    lblBitches: TLabel;
    lblDaysLeft: TLabel;
    lblJacket: TLabel;
    lblJacketLV: TLabel;
    lblKidneys: TLabel;
    lblLocation: TLabel;
    lblMarketLV: TLabel;
    lblName: TLabel;
    lblHP: TLabel;
    lblWeapons: TLabel;
    lvJacket: TListView;
    lvMarket: TListView;
    memMessages: TMemo;
    pbHP: TProgressBar;
    tTrip: TTimer;
    TfraMenu1: TfraMenu;
    lblCheater: TLabel;
    { creation/destruction events }
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    { mouse events }
    procedure btnNewGameClick(Sender: TObject);
    procedure btnHighScoresClick(Sender: TObject);
    procedure ActionButtonClick(Sender: TObject);
    procedure LabelClick(Sender: TObject);
    procedure LabelMouseEnter(Sender: TObject);
    procedure LabelMouseLeave(Sender: TObject);
    procedure LocationButtonClick(Sender: TObject);
    procedure lvJacketClick(Sender: TObject);
    procedure lvMarketClick(Sender: TObject);
    procedure ListViewColumnClick(Sender: TObject; Column: TListColumn);
    procedure memMessagesClick(Sender: TObject);
    { keyboard events }
    procedure FormKeyPress(Sender: TObject; var Key: Char);
    { other events }
    procedure FormClose(Sender: TObject; var Action: TCloseAction);
    procedure FormResize(Sender: TObject);
    procedure tTripTimer(Sender: TObject);
  public
    FQuietExit: Boolean;
    { public procedures }
    procedure HandleGameOver;
    procedure HandleUpdate;
    procedure SetTripping(tripping:Boolean);
  private
    FMemoBounds: array[1..4] of Integer;
    FTripping: Boolean;
    { private procedures }
    procedure CloseRoutine;
    procedure StartTripping;
    procedure StopTripping;
  end;

var
  Main: TMain;

implementation

uses
  formBank, classMessenger, classCity, classDrug, globals, classLocation,
  formArmsDealer, formDoctor, formBrothel, classDopeWars, formOuterSpace,
  classDrugList, formHighScores;

{$R *.dfm}

procedure TMain.FormCreate(Sender: TObject);
begin
  FQuietExit := False;
  FTripping := False;
  TDopeWars.Create(memMessages,gbxLocations,gbxActions);
  dope.GoToLocation(TLocation(gbxLocations.Components[0].Tag));
  dope.Trip.Add(lblCheater.Font,True);
  tTrip.Enabled := True;
  lblCheater.Visible := False;
  Randomize;
end;

procedure TMain.FormDestroy(Sender: TObject);
begin
  tTrip.Enabled := False;
  SetTripping(False);
  dope.Free;
end;

{MOUSE EVENTS}

procedure TMain.ActionButtonClick(Sender: TObject);
begin
  case TButton(Sender).Tag of
    0:  ShowMessage('u fux0r: da button.Tag be 0. whazzat fo stopid??');
    1:  frmBank.ShowForm;
    2:  frmArmsDealer.Show;
    4:  frmDoctor.ShowForm;
    8:  frmBrothel.ShowForm;
    16: frmOuterSpace.ShowForm;
  end;
end;

procedure TMain.btnNewGameClick(Sender: TObject);
begin
  FormDestroy(Self);
  FormCreate(Self);
end;

procedure TMain.LabelClick(Sender: TObject);
var
  lbl: TLabel;
begin
  lbl := TLabel(Sender);
  if (lbl = lblName) then
    dope.People.Player.NewName
  else if (lbl = lblCash) and btnBank.Enabled then
    frmBank.ShowForm
  else if (lbl = lblBitches) and btnBrothel.Enabled then
    frmBrothel.ShowForm
  else if ((lbl = lblHP) or (lbl = lblKidneys)) and btnDoctor.Enabled then
    frmDoctor.ShowForm;
  dope.UpdateForm;
end;

procedure TMain.LabelMouseEnter(Sender: TObject);
var
  L: TLabel;
begin
  L := TLabel(Sender);
  if L.Tag and dope.People.Player.Location.Actions = L.Tag then
    L.Font.Color := clYellow;
end;

procedure TMain.LabelMouseLeave(Sender: TObject);
begin
  TLabel(Sender).Font.Color := clWindowText;
end;

procedure TMain.LocationButtonClick(Sender: TObject);
begin
  SetTripping(False);
  dope.GoToLocation(TLocation(TButton(Sender).Tag));
end;

procedure TMain.lvJacketClick(Sender: TObject);
var
  drug: TDrug;
  cost: integer;
begin
  if lvJacket.Selected <> nil then
  begin
    drug := TDrug(lvJacket.Selected.Data);
    cost := dope.People.Player.Jacket.SellDrug(drug);
    dope.People.Player.Wallet.Earn(cost);
  end;
  dope.UpdateForm;
end;

procedure TMain.lvMarketClick(Sender: TObject);
var
  drug: TDrug;
  cost: integer;
begin
  if lvMarket.Selected <> nil then
  begin
    drug := TDrug(TDrug(lvMarket.Selected.Data));
    cost := dope.People.Player.Jacket.BuyDrug(drug);
    dope.People.Player.Wallet.Spend(cost)
  end;
  dope.UpdateForm;
end;

{KEYBOARD EVENTS}

procedure TMain.FormKeyPress(Sender: TObject; var Key: Char);
begin
  if key in ['a'..'z','A'..'Z'] then
    dope.Cheats.KeyPress(Key);
end;

{OTHER EVENTS}

procedure TMain.FormClose(Sender: TObject; var Action: TCloseAction);
begin
  CloseRoutine;
  Action := caNone;
end;

procedure TMain.CloseRoutine;
begin
  SetTripping(False);
  if FQuietExit
  or ((Application.MessageBox(CloseMsg[1],CloseCap,MB_YESNO) = ID_YES)
  and (Application.MessageBox(CloseMsg[2],CloseCap,MB_YESNO) = ID_YES)
  and (Application.MessageBox(CloseMsg[3],CloseCap,MB_YESNO) = ID_NO)
  and (Application.MessageBox(CloseMsg[4],CloseCap,MB_YESNO) = ID_NO)) then
  begin
    if not FQuietExit then
    begin
      ShowMessage(CloseMsg[5]);
      ShowMessage(CloseMsg[6]);
      ShowMessage(CloseMsg[7]);
    end;
    Application.Terminate;
  end
  else
    ShowMessage(ClosePlay);
end;

procedure TMain.FormResize(Sender: TObject);
const
  G = 8;
var
  W,L: integer;
begin
  { position listviews }
  W := (memMessages.Width - G) div 2;
  L := memMessages.Left;
  lvJacket.Width := W;
  lvJacket.Left := L;
  lvMarket.Width := W;
  lvMarket.Left := L + W + G;
end;

procedure TMain.HandleGameOver;
var
  cap,msg: PAnsiChar;
begin
  StopTripping;
  cap := PAnsiChar(sPlayAgain.cap);
  msg := PAnsiChar(sPlayAgain.msg);
  FormDestroy(Self);
  if Application.MessageBox(msg,cap,MB_YESNO) = ID_NO then
    CloseRoutine;
  FormCreate(Self);
end;

procedure TMain.HandleUpdate;
begin
  // update labels
  with dope.People do
  begin
    lblName.Caption := Format(sName,[Player.Name]);
    lblHP.Caption := Format(sHP,[Player.Health]);
    lblCash.Caption := Format(sCash,[Player.Wallet.Cash]);
    lblJacket.Caption := Format(sJacket,[Player.Jacket.Used,Player.Jacket.Size]);
    lblBank.Caption := Format(sBank,[Player.BankAccount.Balance]);
    lblBitches.Caption := Format(sBitches,[Player.Bitches]);
    lblDaysLeft.Caption := Format(sDaysLeft,[dope.Time]);
    lblLocation.Caption := Format(sLocation,[dope.People.Player.Location.Name]);
    lblKidneys.Caption := Format(sKidneys,[dope.People.Player.Kidneys]);
  end;
  // update health progress bar
  pbHP.Position := dope.People.Player.Health;
  // warn if time's running out
  if dope.Time >= 30
    then lblDaysLeft.Font.Color := clRed
    else lblDaysLeft.Font.Color := clWindowText;
  // warn if nearly dead
  if dope.People.Player.Health <= 10
    then lblHP.Font.Color := clRed
    else lblHP.Font.Color := clWindowText;
  // warn if out of kidneys
  if dope.People.Player.Kidneys = 0
    then lblKidneys.Font.Color := clRed
    else lblKidneys.Font.Color := clWindowText;
  // update listviews
  dope.Market.ShowInListView(lvMarket);
  dope.People.Player.Jacket.ShowInListView(lvJacket);
end;

procedure TMain.tTripTimer(Sender: TObject);
begin
  if dope.Initialized then
    dope.Trip.Process;
end;

procedure TMain.StartTripping;
begin
  with dope.Trip do
  begin
    Add(Main,False);
    Add(lvJacket.Font,False);
    Add(lvMarket.Font,False);
    Add(memMessages.Font,False);
    Add(lvJacket,True);
    Add(lvMarket,True);
    Add(memMessages,True);
    Add(lblCash.Font,True);
    Add(lblName.Font,True);
    Add(gbxLocations.Font,True);
    Add(gbxActions.Font,True);
  end;
end;

procedure TMain.StopTripping;
begin
  with dope.Trip do
  begin
    Remove(Main);
    Remove(lvJacket.Font);
    Remove(lvMarket.Font);
    Remove(memMessages.Font);
    Remove(lvJacket);
    Remove(lvMarket);
    Remove(memMessages);
    Remove(lblCash.Font);
    Remove(lblName.Font);
    Remove(gbxLocations.Font);
    Remove(gbxActions.Font);
  end;
  Main.Update;
end;

procedure TMain.SetTripping(tripping:Boolean);
begin
  if FTripping <> tripping then
    if FTripping then
      StopTripping
    else
      StartTripping;
  FTripping := tripping;
end;

procedure TMain.ListViewColumnClick(Sender: TObject; Column: TListColumn);
begin
  Enabled := False;
  if TListView(Sender) = lvJacket then
    with dope.People.Player.Jacket do
    begin
      case Column.Index of
        0:  Sort(stAmount);
        1:  Sort(stName);
        2:  Sort(stPrice);
      end;
      ShowInListView(lvJacket);
    end
  else
    if TListView(Sender) = lvMarket then
      with dope.Market do
      begin
        case Column.Index of
          0:  Sort(stName);
          1:  Sort(stPrice);
          2:  if dope.Cheats.Demand then
                Sort(stDemand);
        end;
        if not ((Column.Index = 2) and not dope.Cheats.Demand) then
          ShowInListView(lvMarket);
      end;
  Enabled := True;
end;

procedure TMain.memMessagesClick(Sender: TObject);
var m: TMemo;
begin
  m := TMemo(Sender);
  if m.Tag = 0 then
  begin
    m.Tag := 1;
    FMemoBounds[1] := m.Left;
    FMemoBounds[2] := m.Top;
    FMemoBounds[3] := m.Width;
    FMemoBounds[4] := m.Height;
    m.Align := alClient;
    m.BringToFront;
  end
  else if m.Tag = 1 then
  begin
    m.Tag := 0;
    m.Align := alNone;
    m.Left := FMemoBounds[1];
    m.Top := FMemoBounds[2];
    m.Width := FMemoBounds[3];
    m.Height := FMemoBounds[4];
    m.Anchors := [akLeft,akTop,akRight];
  end;
end;

procedure TMain.btnHighScoresClick(Sender: TObject);
begin
  frmHighScores.Visible := True;
end;

initialization
  Randomize;

end.
