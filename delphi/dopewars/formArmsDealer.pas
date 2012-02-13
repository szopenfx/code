unit formArmsDealer;
// arms dealer form

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, ComCtrls,
  classArmsDealer, StdCtrls;

type
  TfrmArmsDealer = class(TForm)
    ListView1: TListView;
    btnViolence: TButton;
    btnClose: TButton;
    procedure FormShow(Sender: TObject);
    procedure ListView1Click(Sender: TObject);
    procedure btnViolenceClick(Sender: TObject);
    procedure btnCloseClick(Sender: TObject);
  end;

var
  frmArmsDealer: TfrmArmsDealer;

implementation

uses
  formViolence, classDopeWars;

{$R *.dfm}

procedure TfrmArmsDealer.FormShow(Sender: TObject);
begin
  dope.People.ArmsDealer.Holster.ShowInListView(ListView1);
end;

procedure TfrmArmsDealer.ListView1Click(Sender: TObject);
begin
  if ListView1.Selected <> nil then
  begin
    dope.People.Player.Holster.BuyWeapon(ListView1.Selected.Data);
    dope.UpdateForm;
  end;
end;

procedure TfrmArmsDealer.btnViolenceClick(Sender: TObject);
begin
  dope.Violence.Reset;
  dope.Violence.AddPerson(dope.People.ArmsDealer);
  frmViolence.ShowForm;
end;

procedure TfrmArmsDealer.btnCloseClick(Sender: TObject);
begin
  Close;
end;

end.
