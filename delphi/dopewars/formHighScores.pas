unit formHighScores;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ComCtrls, classTrip, ExtCtrls;

type
  TfrmHighScores = class(TForm)
    tTripper: TTimer;
    pnlLabel: TPanel;
    pnlListView: TPanel;
    lblPimpest: TLabel;
    lvHighScores: TListView;
    imgIcon1: TImage;
    imgIcon2: TImage;
    procedure FormShow(Sender: TObject);
    procedure FormHide(Sender: TObject);
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure tTripperTimer(Sender: TObject);
  private
    FTrip: TTrip;
  end;

var
  frmHighScores: TfrmHighScores;

implementation

uses
  classDopeWars, formMain;

{$R *.dfm}


procedure TfrmHighScores.FormCreate(Sender: TObject);
begin
  FTrip := TTrip.Create;
  imgIcon1.Picture.Icon.Handle := LoadIcon(hInstance,'MAINICON');
  imgIcon2.Picture.Icon.Handle := LoadIcon(hInstance,'MAINICON');
end;

procedure TfrmHighScores.FormDestroy(Sender: TObject);
begin
  FTrip.Free;
end;

procedure TfrmHighScores.FormShow(Sender: TObject);
begin
  Main.Enabled := False;
  dope.HighScores.ShowInListView(lvHighScores);
  FTrip.Add(lblPimpest.Font,False);
  tTripper.Enabled := True;
end;

procedure TfrmHighScores.FormHide(Sender: TObject);
begin
  tTripper.Enabled := False;
  FTrip.Remove(lblPimpest.Font);
  Main.Enabled := True;
end;

procedure TfrmHighScores.tTripperTimer(Sender: TObject);
begin
  FTrip.Process;
end;

end.
