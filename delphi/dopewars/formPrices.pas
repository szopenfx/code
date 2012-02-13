unit formPrices;
// drug price editor

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls;

type
  TfrmPrices = class(TForm)
    Memo1: TMemo;
    procedure FormCreate(Sender: TObject);
  private
    { Private declarations }
  public
    { Public declarations }
  end;

var
  frmPrices: TfrmPrices;

implementation

uses
  classDopeWars;

{$R *.dfm}

procedure TfrmPrices.FormCreate(Sender: TObject);
begin
  dope.Replicator.ShowInMemo(Memo1);
end;

end.
