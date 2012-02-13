unit classCheats;
// "state machine" for handling cheats

interface

const
  CheatsArr: array[1..9] of string = (
    'prices', 'joostisgod', 'zebug', 'kutjelle', 'exit', 'quit', 'cheat',
    'trip', 'menno'    );

type
  TCheatsEnum = (
    cprices=1, cdemand, cdebug, ckutjelle, cexit, cquit, ccheat, ctrip, cmenno
    );

type
  TCheatRec = record
    Demand: boolean;
    Debug: boolean;
    Average: boolean;
    Cheated: boolean;
    Tripping: boolean;
  end;

type
  TCheats = class(TObject)
  private
    FCheats: TCheatRec;
    FWord: integer;
    FLetter: integer;
    procedure HandleCheat(c:TCheatsEnum);
    function MetaCheat: string;
  public
    constructor Create;
    property Demand: boolean read FCheats.Demand;
    property Debug: boolean read FCheats.Debug;
    property Cheated: boolean read FCheats.Cheated;
    procedure KeyPress(const key:char);
  end;

implementation

uses
  formMain, classDopeWars, formPrices, SysUtils, Forms;

constructor TCheats.Create;
begin
  inherited Create;
  FWord := 0;
  FLetter := 0;
end;

function TCheats.MetaCheat: string;
var
  i: integer;
begin
  result := '';
  for i := Low(CheatsArr) to High(CheatsArr) do
    result := result + CheatsArr[i] + ' ';
end;

procedure TCheats.KeyPress(const key:char);
var
  i: integer;
  s: string;
begin
  if FCheats.Debug then
  begin
    s := 'Read ' + key + ' (word: ' + IntToStr(FWord) + ', letter: ' + IntToStr(FLetter) + ')';
    dope.Messenger.AddMessage(s);
  end;
  if FWord > 0 then // more than 1 letter of a cheat has been entered
    if CheatsArr[FWord][FLetter] = key then
    begin
      Inc(FLetter);
      if FLetter > Length(CheatsArr[FWord]) then
        HandleCheat(TCheatsEnum(FWord));
    end
    else
    begin
      FWord := 0;
      FLetter := 0;
      KeyPress(key);
    end
  else // check if keypress was one of the 1st letters of the cheats
    if FWord = 0 then
      for i := Low(CheatsArr) to High(CheatsArr) do
        if CheatsArr[i][1] = key then
        begin
          FWord := i;
          FLetter := 2;
        end;
end;

procedure TCheats.HandleCheat(c:TCheatsEnum);
          procedure Toggle(var x:boolean);
          begin
            x := not x;
          end;
const
  MENNO: TOBJECT = NIL;
begin
  if c in [ cprices, cdemand, ccheat ] then
  begin
    FCheats.Cheated := True;
    dope.Trip.Add(Main.lblCheater.Font,True);
    Main.lblCheater.Visible := True;
  end;

  FWord := 0;
  FLetter := 0;

  case c of
    cmenno:
      begin
        Menno.Destroy;
      end;
    cprices:
      begin
        Toggle(FCheats.Average);
        frmPrices.Visible := FCheats.Average;
        Main.SetFocus;
      end;
    cdemand:
      begin
        Toggle(FCheats.Demand);
        dope.Market.ShowInListView(Main.lvMarket);
        if FCheats.Demand
          then dope.Messenger.AddMessage('Showing demand levels')
          else dope.Messenger.AddMessage('Not showing demand levels');
      end;
    cdebug:
      begin
        Toggle(FCheats.Debug);
        dope.Trip.Debug := FCheats.Debug;
        if FCheats.Debug
          then dope.Messenger.AddMessage('Debugging enabled')
          else dope.Messenger.AddMessage('Debugging disabled');
      end;
    ckutjelle:
      begin
        dope.Messenger.AddMessage('Kutmarrokaan');
        dope.People.Player.KutJelle;
        dope.UpdateForm;
        dope.GameOver;
      end;
    cexit, cquit:
      begin
        Toggle(Main.FQuietExit);
        dope.Messenger.AddMessage('l8r');
        Main.Update;
        Main.Close;
      end;
    ccheat:
      begin
        dope.Messenger.AddMessage('Lame squared.');
        dope.Messenger.AddMessage(MetaCheat);
      end;
    ctrip:
      begin
        Toggle(FCheats.Tripping);
        if FCheats.Tripping then
          dope.Messenger.AddMessage('Whoopee!');
        Main.SetTripping(FCheats.Tripping);
      end;
  end;
end;

end.
