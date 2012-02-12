object frmHighScores: TfrmHighScores
  Left = 310
  Top = 205
  Width = 590
  Height = 565
  BorderIcons = [biSystemMenu]
  Caption = 'dope wars pimpest'
  Color = clBtnFace
  Font.Charset = DEFAULT_CHARSET
  Font.Color = clWindowText
  Font.Height = -11
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  OnHide = FormHide
  OnShow = FormShow
  PixelsPerInch = 96
  TextHeight = 13
  object pnlLabel: TPanel
    Left = 0
    Top = 0
    Width = 582
    Height = 41
    Align = alTop
    TabOrder = 0
    object lblPimpest: TLabel
      Left = 106
      Top = 1
      Width = 370
      Height = 39
      Align = alClient
      Alignment = taCenter
      Caption = 'DOPE WARS PIMPEST'
      Font.Charset = DEFAULT_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = 'Tahoma'
      Font.Style = [fsBold]
      ParentFont = False
    end
    object imgIcon1: TImage
      Left = 1
      Top = 1
      Width = 105
      Height = 39
      Align = alLeft
      Center = True
    end
    object imgIcon2: TImage
      Left = 476
      Top = 1
      Width = 105
      Height = 39
      Align = alRight
      Center = True
    end
  end
  object pnlListView: TPanel
    Left = 0
    Top = 41
    Width = 582
    Height = 496
    Align = alClient
    TabOrder = 1
    object lvHighScores: TListView
      Left = 1
      Top = 1
      Width = 580
      Height = 494
      Align = alClient
      Columns = <
        item
          Caption = '#'
        end
        item
          Alignment = taCenter
          Caption = 'Name'
          Width = 250
        end
        item
          Alignment = taRightJustify
          Caption = 'Cash ('#402')'
          Width = 100
        end
        item
          Alignment = taRightJustify
          Caption = 'Health (%)'
          Width = 100
        end>
      TabOrder = 0
      ViewStyle = vsReport
    end
  end
  object tTripper: TTimer
    Enabled = False
    Interval = 20
    OnTimer = tTripperTimer
  end
end
