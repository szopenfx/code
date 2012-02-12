unit classDopeWars;
// game engine

interface

uses
  Messages, Windows, StdCtrls,
  classReplicator, classJacket, classMarket, classLuck, classHighScores,
  classDrug, classCheats, classMessenger, classPeople, classCity,
  classViolence, classLocation, classTrip;

type
  TDopeWars = class(TObject)
  private
    FInitialized: boolean;
    FTime       : integer;
    FCity       : TCity;
    FReplicator : TReplicator;
    FPeople     : TPeople;
    FMarket     : TMarket;
    FLuck       : TLuck;
    FHighScores : THighScores;
    FCheats     : TCheats;
    FMessenger  : TMessenger;
    FViolence   : TViolence;
    FTrip       : TTrip;
  public
    constructor Create(memo:TMemo; citygbx,actiongbx:TGroupBox);
    destructor  Destroy; override;
    procedure   GoToLocation(ALocation:TLocation);
    procedure   GameOver;
    procedure   UpdateForm;
    procedure   StartTrip;
    property    Time        : integer           read FTime;
    property    City        : TCity             read FCity;
    property    Replicator  : TReplicator       read FReplicator;
    property    People      : TPeople           read FPeople;
    property    Market      : TMarket           read FMarket;
    property    HighScores  : THighScores       read FHighScores;
    property    Cheats      : TCheats           read FCheats;
    property    Messenger   : TMessenger        read FMessenger;
    property    Violence    : TViolence         read FViolence;
    property    Trip        : TTrip             read FTrip;
    property    Initialized : boolean           read FInitialized;
  end;

var
  dope: TDopeWars = nil;

implementation

uses
  Dialogs, SysUtils, formMain,
  classScore, formHighScores, classBankAccount, globals;

(* memo is for the messages of TMessenger,
   groupbox will contain the buttons of TLocation *)
constructor TDopeWars.Create(memo:TMemo; citygbx,actiongbx:TGroupBox);
begin
  inherited Create;
  dope := Self;
  FTime := 0;
  FCity := TCity.Create(citygbx,actiongbx);
  FReplicator := TReplicator.Create;
  FPeople := TPeople.Create;
  FMarket := TMarket.Create;
  FLuck := TLuck.Create;
  FHighScores := THighScores.Create;
  FCheats := TCheats.Create;
  FMessenger := TMessenger.Create(memo);
  FViolence := TViolence.Create;
  FTrip := TTrip.Create;
  FInitialized := True;
end;

destructor TDopeWars.Destroy;
begin
  FInitialized := False;
  FTrip.Free;
  FViolence.Free;
  FMessenger.Free;
  FCheats.Free;
  FHighScores.Free;
  FLuck.Free;
  FMarket.Free;
  FPeople.Free;
  FReplicator.Free;
  FCity.Free;
  dope := nil;
  inherited Destroy;
end;

procedure TDopeWars.GoToLocation(ALocation:TLocation);
begin
  FTime := FTime + 1;
  FMessenger.AddMessage(Format(sGoto,[ALocation.Name]));
  FCity.SetDisabled(ALocation);
  FPeople.Player.Location := ALocation;
  FMarket.SetDrugMenu;
  FLuck.WhenPlayerMoves;
  FPeople.AddInterest;
  FPeople.BringOutTheDead;
  UpdateForm;
  GameOver;
end;

procedure TDopeWars.GameOver;
var
  g_over: boolean;
  n: string;
  s,h: integer;
begin
  g_over := (FTime = 32) or FPeople.Player.IsDead;
  if g_over then
  begin
    n := FPeople.Player.Name;
    s := FPeople.Player.Wallet.Cash + FPeople.Player.BankAccount.Balance;
    h := FPeople.Player.Health;
    FHighScores.AddScore(TScore.Create(n,s,h));
    frmHighScores.Visible := True;
    Main.HandleGameover;
  end;
end;

procedure TDopeWars.UpdateForm;
begin
  Main.HandleUpdate;
end;

procedure TDopeWars.StartTrip;
begin
  Main.SetTripping(True);
end;

end.
