unit classTrip;

interface

uses
  Graphics, Contnrs;

type
  TTripRec = record
    Obj: TObject;
    Inv: Boolean;
    Orig: TColor;
  end;
  TTripArr = array of TTripRec;

type
  TTrip = class(TObject)
  private
    FCtrl: TTripArr;
    FColor: TColor;
    FInvColor: TColor;
    FState: Integer;
    FDebug: Boolean;
    procedure NextColor;
    procedure SetColor(o:TObject; c:TColor);
    function GetColor(o:TObject): TColor;
  public
    constructor Create;
    destructor Destroy; override;
    procedure Add(o:TObject; inv:Boolean=False);
    procedure Remove(o:TObject);
    procedure Process;
    property Debug: Boolean read FDebug write FDebug;
  end;

implementation

uses
  Controls, StdCtrls, Forms, ComCtrls, Classes, SysUtils,
  classDopeWars;

constructor TTrip.Create;
begin
  inherited Create;
  FColor := $00ff0000;
  FInvColor := $0000ffffff;
  FState := 1;
end;

destructor TTrip.Destroy;
var
  i: integer;
begin
  for i := Length(FCtrl) - 1 downto 0 do
    Remove(FCtrl[i].Obj);
  SetLength(FCtrl,0);
  inherited Destroy;
end;

procedure TTrip.SetColor(o:TObject; c:TColor);
begin
  if o is TFont then TFont(o).Color := c else
  if o is TListView then TListView(o).Color := c else
  if o is TGroupBox then TGroupBox(o).Color := c else
  if o is TMemo then TMemo(o).Color := c else
  if o is TLabel then TLabel(o).Color := c else
  if o is TForm then TForm(o).Color := c;
end;

function TTrip.GetColor(o:TObject): TColor;
begin
  result := clBlack;
  if o is TFont then result := TFont(o).Color else
  if o is TListView then result := TListView(o).Color else
  if o is TGroupBox then result := TGroupBox(o).Color else
  if o is TMemo then result := TMemo(o).Color else
  if o is TLabel then result := TLabel(o).Color else
  if o is TForm then result := TForm(o).Color;
end;

procedure TTrip.Add(o:TObject; inv:Boolean=False);
  procedure AddToArray(var arr:TTripArr; o:TObject; inv:Boolean; orig:TColor);
  var
    len: integer;
  begin
    len := Length(arr);
    SetLength(arr,len+1);
    arr[len].Obj := o;
    arr[len].Inv := inv;
    arr[len].Orig := orig;
  end;
begin
  AddToArray(FCtrl,o,inv,GetColor(o));
end;

procedure TTrip.Remove(o:TObject);
  procedure RemoveFromArray(var arr:TTripArr; o:TObject);
  var
    i,j: integer;
  begin
    i := 0;
    while (arr[i].Obj <> o) and (i < Length(arr)) do
      Inc(i);
    if i < Length(arr) then
    begin
      SetColor(arr[i].Obj,arr[i].Orig);
      for j := i to Length(arr) - 2 do
        arr[j] := arr[j+1];
      SetLength(arr,Length(arr)-1);
    end;
  end;
begin
  RemoveFromArray(FCtrl,o);
end;

procedure TTrip.Process;
var
  i: integer;
begin
  NextColor;
  for i := 0 to Length(FCtrl) - 1 do
    if FCtrl[i].Inv then
      SetColor(FCtrl[i].Obj,FInvColor)
    else
      SetColor(FCtrl[i].Obj,FColor);
end;

procedure TTrip.NextColor;
const
  debug_str = 'state=%d (%s) color=%.8x inv_color=%.8x';
  debug_type: array[1..6] of string
    = ('R+', 'B-', 'G+', 'R-', 'B+', 'G-');
  color_max: array[1..6] of TColor =
    ($ff00ff, $0000ff, $00ffff, $00ff00, $ffff00, $ff0000);
  color_delta: array[1..6] of TColor =
    ($000011, -$110000, $001100, -$000011, $110000, -$001100);
var
  reached_max: Boolean;
begin
  reached_max := FColor = color_max[FState];
  if reached_max then
    if FState < 6
      then Inc(FState)
      else FState := 1
  else
    FColor := FColor + color_delta[FState];

  FInvColor := (not FColor) and $00ffffff;

  if FDebug then
    dope.Messenger.AddMessage(
      Format(debug_str,[FState,debug_type[FState],FColor,FInvColor])
      );
end;

end.
