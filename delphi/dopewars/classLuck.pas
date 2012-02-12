unit classLuck;
// class for good & bad luck

interface

uses
  classReplicator, classJacket;

type
  TChance = record
    msg: string;
    drug: string;
  end;

type
  TLuck = class(TObject)
  private
    procedure GetLose(msg,drugname:string; amount:integer);
    procedure MarketShortage(thing:TChance);
    procedure MarketAbundance(thing:TChance);
    procedure GetDrug(thing:TChance);
    procedure LoseDrug(thing:TChance);

  public
    constructor Create;
    procedure WhenPlayerMoves;
  end;

implementation

uses
  Dialogs,
  classDopeWars, classDrug;

var
  get_drug: array[0..3] of TChance =
    ( (msg: 'You find some speed on a dead guy in the bus!'; drug: 'Speed'),
      (msg: 'A mate gives you some pills!'; drug: 'XTC'),
      (msg: 'A Mexican gives you a bag of weed!'; drug: 'Weed'),
      (msg: 'It''s that time again... Everybody loves the park! Praise the tun!'; drug: 'Shroomies')
    );
  lose_drug: array[0..0] of TChance =
    ( (msg: 'Your mother made some hash brownies. They were great!'; drug: 'Hash')
    );
  market_cheap: array[0..4] of TChance =
    ( (msg: 'The Northern Alliance are kicking the Taliban''s ass!'; drug: 'Opium'),
      (msg: 'The Marakesh express has arrived!'; drug: 'Hash'),
      (msg: 'Some thugs raided the pharmacy store!'; drug: 'Ludes'),
      (msg: 'Somebody robbed a doctor!'; drug: 'Morphine'),
      (msg: 'Somebody broke into the hospital!'; drug: 'Ketamine')
    );
  market_expensive: array[0..5] of TChance =
    ( (msg: 'There''s a hippie festival...'; drug: 'Opium'),
      (msg: 'The cops busted the baddest coke dealer!'; drug: 'Cocaine'),
      (msg: 'A group of 100 bald-headed hollow-eyed ravers stares intensely at you.'; drug: 'Speed'),
      (msg: 'The cops made a big heroin bust!'; drug: 'Heroin'),
      (msg: 'The Taliban are kicking the Nortern Alliance''s ass!'; drug: 'Opium'),
      (msg: 'Reggae party!'; drug: 'Weed')
    );

constructor TLuck.Create;
begin
  inherited Create;
end;

procedure TLuck.WhenPlayerMoves;
begin
{  case Random(3000) of
    0..200:    GetLose('You find 10 speed on a dead bloke in the metro!','Speed',10);
    201..400:  GetLose('A buddy gives you 5 pills!','XTC',5);
    401..601:  GetLose('Your mate gives you a bag of weed!','Weed',3);
    950..1000: GetLose('It''s that time again... You love the park!','Shroomies',10);
  end;}
  case Random(2) of
    0:  GetDrug(get_drug[Random(Length(get_drug))]);
    1:  LoseDrug(lose_drug[Random(Length(get_drug))]);
  end;
  case Random(5) of
    0..1: MarketAbundance(market_cheap[Random(Length(market_cheap))]);
    2..4: MarketShortage(market_expensive[Random(Length(market_expensive))]);
  end;
end;

procedure TLuck.GetDrug(thing:TChance);
var
  drug, drug2: TDrug;
  amount: integer;
begin
  dope.Messenger.AddMessage(thing.msg);
  drug := dope.People.Player.Jacket.FindDrug(thing.drug);
  amount := 1 + Random(10);
  if drug = nil then
  begin
    drug := dope.Replicator.Duplicate(thing.drug,amount);
    drug.ZeroDemand;
    dope.People.Player.Jacket.AddDrug(drug);
  end
  else
  begin
    drug2 := dope.Replicator.Duplicate(thing.drug, amount);
    drug2.ZeroDemand;
    drug.Add(drug2);
    drug2.Free;
  end;
end;

procedure TLuck.LoseDrug(thing:TChance);
var
  drug, drug2: TDrug;
begin
  drug := dope.People.Player.Jacket.FindDrug(thing.drug);
  if drug <> nil then
  begin
    drug2 := dope.Replicator.Duplicate(thing.drug, Random(10) + 1);
    drug.Substract(drug2);
    if drug.Amount < 1 then
      dope.People.Player.Jacket.RemoveDrug(drug);
    drug2.Free;
  end;
end;

procedure TLuck.GetLose(msg,drugname:string; amount:integer);
var
  d,d2:TDrug;
begin
  {$MESSAGE warn 'there''s something strange in TLuck.GetLose()'}
  dope.Messenger.AddMessage(msg);
  d := dope.People.Player.Jacket.FindDrug(drugname);
  if d = nil then
  begin
    d := dope.Replicator.Duplicate(drugname,amount);
    d.Amount := amount;
    d.ZeroDemand;
    dope.People.Player.Jacket.AddDrug(d);
  end
  else
    if amount > 0 then
    begin
      d2 := dope.Replicator.Duplicate(drugname,amount);
      d2.ZeroDemand;
      d.Add(d2);
      d2.Free;
    end // ...
end;

procedure TLuck.MarketAbundance(thing:TChance);
var
  drug: TDrug;
begin
  drug := dope.Market.FindDrug(thing.drug);
  if drug <> nil then
  begin
    dope.Messenger.AddMessage(thing.msg);
    drug.Abundance;
  end;
end;

procedure TLuck.MarketShortage(thing:TChance);
var
  drug: TDrug;
begin
  drug := dope.Market.FindDrug(thing.drug);
  if drug <> nil then
  begin
    dope.Messenger.AddMessage(thing.msg);
    drug.Shortage;
  end;
end;

end.


