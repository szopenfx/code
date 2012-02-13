unit frameMenu;
// the menu bar on top of the form & the listview pop-up menus

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms, 
  Dialogs, Menus;

type
  TfraMenu = class(TFrame)
    pmJacket: TPopupMenu;
    Use1: TMenuItem;
    procedure Use1Click(Sender: TObject);
  end;

implementation

uses
  formMain, formBank, formHighScores, formArmsDealer, classDrug, classDopeWars;

{$R *.dfm}

procedure TfraMenu.Use1Click(Sender: TObject);
begin
  if Main.lvJacket.Selected <> nil then
  begin
    dope.People.Player.UseDrug(TDrug(Main.lvJacket.Selected.Data));
    dope.UpdateForm;
  end;
end;

end.
