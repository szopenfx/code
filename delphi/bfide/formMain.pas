unit formMain;

interface

uses
  Windows, Messages, SysUtils, Variants, Classes, Graphics, Controls, Forms,
  Dialogs, StdCtrls, ComCtrls, ExtCtrls, classBrainfuck, ToolWin, ImgList;

type
  TfrmMain = class(TForm)
    Status: TStatusBar;
    pcMemory: TPageControl;
    tabData: TTabSheet;
    tabCode: TTabSheet;
    lvData: TListView;
    pnlButtons: TPanel;
    pnlSource: TPanel;
    lvCode: TListView;
    reSource: TRichEdit;
    pcIO: TPageControl;
    tabOutput: TTabSheet;
    tabInput: TTabSheet;
    memOutput: TMemo;
    memInput: TMemo;
    Splitter1: TSplitter;
    Splitter2: TSplitter;
    ToolBar1: TToolBar;
    tbLoad: TToolButton;
    ImageList1: TImageList;
    tbSave: TToolButton;
    tbData: TToolButton;
    tbReset: TToolButton;
    tbRun: TToolButton;
    tbStep: TToolButton;
    tbStop: TToolButton;
    ToolButton1: TToolButton;
    ToolButton2: TToolButton;
    ToolButton3: TToolButton;
    OpenDialog1: TOpenDialog;
    SaveDialog1: TSaveDialog;
    tbStatus: TToolButton;
    pnlDataDockSite: TPanel;
    pnlDataDockClient: TPanel;
    pnlCodeDockSite: TPanel;
    pnlCodeDockClient: TPanel;
    btnData: TButton;
    btnCode: TButton;
    procedure FormCreate(Sender: TObject);
    procedure FormDestroy(Sender: TObject);
    procedure lvDataData(Sender: TObject; Item: TListItem);
    procedure lvCodeData(Sender: TObject; Item: TListItem);
    procedure reSourceChange(Sender: TObject);
    procedure tbStopClick(Sender: TObject);
    procedure tbStepClick(Sender: TObject);
    procedure tbRunClick(Sender: TObject);
    procedure tbResetClick(Sender: TObject);
    procedure tbLoadClick(Sender: TObject);
    procedure tbSaveClick(Sender: TObject);
    procedure pnlClose(Sender: TObject; var CloseAction: TCloseAction);
    procedure pnlEndDock(Sender, Target: TObject; X, Y: Integer);
    procedure pnlGetSiteInfo(Sender: TObject; DockClient: TControl;
      var InfluenceRect: TRect; MousePos: TPoint; var CanDock: Boolean);
    procedure btnDataClick(Sender: TObject);
    procedure btnCodeClick(Sender: TObject);
  public
    procedure UpdateForm;
  end;

type
  TCloseDockForm = class(TCustomDockForm)
  public
    property OnClose;
  end;

var
  frmMain: TfrmMain;

implementation

{$R *.dfm}


procedure TfrmMain.FormCreate(Sender: TObject);
begin
  TBrainfuck.Create;
  lvData.Items.Count := 30000;
  pnlDataDockClient.FloatingDockSiteClass := TCloseDockForm;
  pnlDataDockClient.ManualFloat(pnlDataDockClient.BoundsRect);
  pnlDataDockClient.ManualDock(pnlDataDockSite,nil,alClient);
  pnlCodeDockClient.FloatingDockSiteClass := TCloseDockForm;
  pnlCodeDockClient.ManualFloat(pnlCodeDockClient.BoundsRect);
  pnlCodeDockClient.ManualDock(pnlCodeDockSite,nil,alClient)
end;

procedure TfrmMain.FormDestroy(Sender: TObject);
begin
  bf.Free;
end;

procedure TfrmMain.lvDataData(Sender: TObject; Item: TListItem);
begin
  Item.Caption := IntToStr(Item.Index);
  Item.SubItems.Add(IntToStr(Ord(bf.data[Item.Index])));
  Item.SubItems.Add(bf.data[Item.Index]);
end;

procedure TfrmMain.lvCodeData(Sender: TObject; Item: TListItem);
begin
  Item.Caption := IntToStr(Item.Index);
  Item.SubItems.Add(bf.code[Item.Index]);
  Item.SubItems.Add(Format('%d:%d',[bf.line[Item.Index],bf.col[Item.Index]]));
end;

procedure TfrmMain.UpdateForm;
begin
  tbStep.Enabled := bf.IsReady;
  tbRun.Enabled := bf.IsReady;
  tbStop.Enabled := bf.running;
  tbreset.Enabled := not bf.running;

  if not bf.running then
  begin
    lvCode.Items.Count := bf.codesize;
    lvCode.UpdateItems(0,bf.codesize - 1);
  end;

  if bf.ip < bf.codesize then
    if tbStatus.Down then
    with Status do
    begin
      Panels[0].Text := Format('ip: %d (%s)',[bf.ip,bf.code[bf.ip]]);
      Panels[1].Text := Format('ptr: %d (%d)',[bf.ptr,Ord(bf.data[bf.ptr])]);
    end;

  Update;
  if bf.running then
    Application.ProcessMessages;
end;

procedure TfrmMain.reSourceChange(Sender: TObject);
begin
  bf.AddCode(reSource.Text);
  lvCode.UpdateItems(0,bf.codesize-1);
  UpdateForm;
end;

procedure TfrmMain.tbStopClick(Sender: TObject);
begin
  bf.stop := True;
end;

procedure TfrmMain.tbStepClick(Sender: TObject);
begin
  bf.Execute;
  UpdateForm;
end;

procedure TfrmMain.tbRunClick(Sender: TObject);
begin
  bf.Run;
  UpdateForm;
end;

procedure TfrmMain.tbResetClick(Sender: TObject);
begin
  bf.Reset;
  UpdateForm;
  lvData.UpdateItems(0,29999);
  memOutput.Lines.Clear;
  bf.AddCode(reSource.Text);
  lvData.UpdateItems(0,bf.codesize-1);
end;

procedure TfrmMain.tbLoadClick(Sender: TObject);
begin
  if OpenDialog1.Execute then
  begin
    reSource.Lines.Clear;
    reSource.Lines.LoadFromFile(OpenDialog1.FileName);
  end;
end;

procedure TfrmMain.tbSaveClick(Sender: TObject);
begin
  if SaveDialog1.Execute then
  begin
    ShowMessage('not yet');
  end;
end;

procedure TfrmMain.pnlClose(Sender: TObject; var CloseAction: TCloseAction);
var
  AControl: TControl;
begin
  // dock control to where it came from
  AControl := TCustomDockForm(Sender).Controls[0];
  if AControl = pnlDataDockClient then
    AControl.ManualDock(pnlDataDockSite,nil,alClient);
  if AControl = pnlCodeDockClient then
    AControl.ManualDock(pnlCodeDockSite,nil,alClient)
end;

procedure TfrmMain.pnlEndDock(Sender, Target: TObject; X, Y: Integer);
begin
  if TPanel(Sender).Floating then
    TCloseDockForm(TPanel(Sender).Parent).OnClose := pnlClose;
end;

procedure TfrmMain.pnlGetSiteInfo(Sender: TObject; DockClient: TControl;
  var InfluenceRect: TRect; MousePos: TPoint; var CanDock: Boolean);
begin
  CanDock := ((Sender = pnlDataDockSite) and (DockClient = pnlDataDockClient))
    or ((Sender = pnlCodeDockSite) and (DockClient = pnlCodeDockClient));
end;

procedure TfrmMain.btnDataClick(Sender: TObject);
begin
  pnlDataDockClient.Visible := True;
end;

procedure TfrmMain.btnCodeClick(Sender: TObject);
begin
  pnlCodeDockClient.Visible := True;
end;

end.
