unit formOuterSpace;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, CheckLst, ExtCtrls;

type
  TfrmOuterSpace = class(TForm)
    lblDrugs: TLabel;
    cbxDrugs: TCheckListBox;
    Panel1: TPanel;
    Button1: TButton;
    Button2: TButton;
    procedure Button2Click(Sender: TObject);
    procedure Button1Click(Sender: TObject);
  public
    procedure ShowForm;
  end;

var
  frmOuterSpace: TfrmOuterSpace;

implementation

uses
  classDopeWars, classDrug;

{$R *.dfm}

procedure TfrmOuterSpace.ShowForm;
var
  i: integer;
begin
  cbxDrugs.Items.Clear;
  with dope.People.Player do
    for i := 0 to Jacket.Count - 1 do
      cbxDrugs.Items.AddObject(Jacket[i].Name,Jacket[i]);
  Visible := True;
end;

procedure TfrmOuterSpace.Button2Click(Sender: TObject);
begin
  Visible := False;
end;

procedure TfrmOuterSpace.Button1Click(Sender: TObject);
var
  i: integer;
begin
  for i := 0 to cbxDrugs.Items.Count - 1 do
    if cbxDrugs.Checked[i] then
      if dope.People.Player.IsAlive then
        dope.People.Player.UseDrug(TDrug(cbxDrugs.Items.Objects[i]));
  Visible := False;
end;

end.
