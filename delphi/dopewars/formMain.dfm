object Main: TMain
  Left = 298
  Top = 142
  Width = 719
  Height = 775
  Caption = 'dope wars - beta version'
  Color = clBtnFace
  DockSite = True
  Font.Charset = ANSI_CHARSET
  Font.Color = clWindowText
  Font.Height = -13
  Font.Name = 'Tahoma'
  Font.Style = []
  OldCreateOrder = False
  ShowHint = True
  OnClose = FormClose
  OnCreate = FormCreate
  OnDestroy = FormDestroy
  OnKeyPress = FormKeyPress
  OnResize = FormResize
  DesignSize = (
    711
    748)
  PixelsPerInch = 96
  TextHeight = 16
  object lblJacketLV: TLabel
    Left = 160
    Top = 112
    Width = 138
    Height = 16
    Caption = 'Drugs in your &Jacket:'
    FocusControl = lvJacket
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object lblMarketLV: TLabel
    Left = 440
    Top = 112
    Width = 113
    Height = 16
    Caption = 'Drugs on &Market:'
    FocusControl = lvMarket
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object lblCheater: TLabel
    Left = 577
    Top = 96
    Width = 128
    Height = 33
    Alignment = taRightJustify
    Caption = 'CHEATER'
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -27
    Font.Name = 'Tahoma'
    Font.Style = [fsBold]
    ParentFont = False
  end
  object lvJacket: TListView
    Left = 160
    Top = 136
    Width = 273
    Height = 604
    Hint = 'Click on a drug to sell it'
    Anchors = [akLeft, akTop, akBottom]
    Columns = <
      item
        Caption = 'amount'
        Width = 60
      end
      item
        Caption = 'drug'
        Width = 135
      end
      item
        Alignment = taRightJustify
        Caption = 'price ('#402')'
        Width = 65
      end>
    Ctl3D = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = []
    RowSelect = True
    ParentFont = False
    PopupMenu = TfraMenu1.pmJacket
    TabOrder = 0
    ViewStyle = vsReport
    OnClick = lvJacketClick
    OnColumnClick = ListViewColumnClick
    OnKeyPress = FormKeyPress
  end
  object lvMarket: TListView
    Left = 440
    Top = 136
    Width = 265
    Height = 604
    Hint = 'Click on a drug to buy it'
    Anchors = [akLeft, akTop, akBottom]
    Columns = <
      item
        Caption = 'drug'
        Width = 140
      end
      item
        Alignment = taRightJustify
        Caption = 'price ('#402')'
        Width = 65
      end
      item
        Alignment = taRightJustify
        Width = 45
      end>
    Ctl3D = False
    RowSelect = True
    TabOrder = 1
    ViewStyle = vsReport
    OnClick = lvMarketClick
    OnColumnClick = ListViewColumnClick
    OnKeyPress = FormKeyPress
  end
  object gbxLocations: TGroupBox
    Left = 8
    Top = 232
    Width = 145
    Height = 49
    Caption = 'Jet'
    Ctl3D = False
    ParentCtl3D = False
    TabOrder = 2
  end
  object btnNewGame: TButton
    Left = 16
    Top = 712
    Width = 129
    Height = 25
    Caption = 'nOo game'
    TabOrder = 3
    OnClick = btnNewGameClick
  end
  object memMessages: TMemo
    Left = 160
    Top = 8
    Width = 545
    Height = 89
    Anchors = [akLeft, akTop, akRight]
    Ctl3D = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -11
    Font.Name = 'Tahoma'
    Font.Style = []
    Lines.Strings = (
      'lbxMessages')
    ParentCtl3D = False
    ParentFont = False
    ReadOnly = True
    ScrollBars = ssVertical
    TabOrder = 4
    OnClick = memMessagesClick
  end
  object gbxStats: TGroupBox
    Left = 8
    Top = 0
    Width = 145
    Height = 225
    Ctl3D = False
    ParentCtl3D = False
    TabOrder = 5
    object lblLocation: TLabel
      Left = 8
      Top = 200
      Width = 60
      Height = 16
      Caption = 'lblLocation'
    end
    object lblCash: TLabel
      Tag = 2
      Left = 8
      Top = 72
      Width = 98
      Height = 33
      Cursor = crHandPoint
      Caption = 'lblCash'
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -27
      Font.Name = 'Tahoma'
      Font.Style = [fsBold]
      ParentFont = False
      OnClick = LabelClick
      OnMouseEnter = LabelMouseEnter
      OnMouseLeave = LabelMouseLeave
    end
    object lblBank: TLabel
      Tag = 2
      Left = 8
      Top = 112
      Width = 40
      Height = 16
      Cursor = crHandPoint
      Caption = 'lblBank'
      OnClick = LabelClick
      OnMouseEnter = LabelMouseEnter
      OnMouseLeave = LabelMouseLeave
    end
    object lblDaysLeft: TLabel
      Left = 8
      Top = 24
      Width = 61
      Height = 16
      Caption = 'lblDaysLeft'
    end
    object lblJacket: TLabel
      Tag = 4
      Left = 8
      Top = 40
      Width = 48
      Height = 16
      Cursor = crHandPoint
      Caption = 'lblJacket'
      OnClick = LabelClick
      OnMouseEnter = LabelMouseEnter
      OnMouseLeave = LabelMouseLeave
    end
    object lblName: TLabel
      Tag = 1
      Left = 8
      Top = 8
      Width = 49
      Height = 16
      Cursor = crHandPoint
      Caption = 'lblName'
      Font.Charset = ANSI_CHARSET
      Font.Color = clWindowText
      Font.Height = -13
      Font.Name = 'Tahoma'
      Font.Style = [fsBold]
      ParentFont = False
      OnClick = LabelClick
      OnMouseEnter = LabelMouseEnter
      OnMouseLeave = LabelMouseLeave
    end
    object lblHP: TLabel
      Tag = 3
      Left = 8
      Top = 160
      Width = 28
      Height = 16
      Cursor = crHandPoint
      Caption = 'lblHP'
      OnClick = LabelClick
      OnMouseEnter = LabelMouseEnter
      OnMouseLeave = LabelMouseLeave
    end
    object lblBitches: TLabel
      Tag = 4
      Left = 8
      Top = 128
      Width = 53
      Height = 16
      Cursor = crHandPoint
      Caption = 'lblBitches'
      OnClick = LabelClick
      OnMouseEnter = LabelMouseEnter
      OnMouseLeave = LabelMouseLeave
    end
    object lblWeapons: TLabel
      Left = 224
      Top = 40
      Width = 16
      Height = 16
      Caption = '    '
    end
    object lblKidneys: TLabel
      Left = 8
      Top = 144
      Width = 56
      Height = 16
      Caption = 'lblKidneys'
    end
    object pbHP: TProgressBar
      Left = 8
      Top = 181
      Width = 129
      Height = 17
      Smooth = True
      TabOrder = 0
    end
  end
  object gbxActions: TGroupBox
    Left = 8
    Top = 523
    Width = 145
    Height = 145
    Caption = '&Visit'
    Ctl3D = False
    Font.Charset = ANSI_CHARSET
    Font.Color = clWindowText
    Font.Height = -13
    Font.Name = 'Tahoma'
    Font.Style = []
    ParentCtl3D = False
    ParentFont = False
    TabOrder = 6
    object btnBank: TButton
      Tag = 1
      Left = 8
      Top = 16
      Width = 129
      Height = 25
      Caption = 'Bank'
      TabOrder = 0
      OnClick = ActionButtonClick
    end
    object btnArmsDealer: TButton
      Tag = 2
      Left = 8
      Top = 40
      Width = 129
      Height = 25
      Caption = 'Arms-R-us'
      TabOrder = 1
      OnClick = ActionButtonClick
    end
    object btnDoctor: TButton
      Tag = 4
      Left = 8
      Top = 64
      Width = 129
      Height = 25
      Caption = 'Doctor'
      TabOrder = 2
      OnClick = ActionButtonClick
    end
    object btnBrothel: TButton
      Tag = 8
      Left = 8
      Top = 88
      Width = 129
      Height = 25
      Caption = 'Brothel'
      TabOrder = 3
      OnClick = ActionButtonClick
    end
    object btnOuterSpace: TButton
      Tag = 16
      Left = 8
      Top = 112
      Width = 129
      Height = 25
      Caption = 'Space'
      TabOrder = 4
      OnClick = ActionButtonClick
    end
  end
  object btnHighScores: TButton
    Left = 16
    Top = 680
    Width = 129
    Height = 25
    Caption = 'HiGH SC0REs'
    TabOrder = 7
    OnClick = btnHighScoresClick
  end
  inline TfraMenu1: TfraMenu
    Left = 16
    Top = 456
    Width = 97
    Height = 57
    TabOrder = 8
    Visible = False
  end
  object tTrip: TTimer
    Interval = 100
    OnTimer = tTripTimer
    Left = 56
    Top = 464
  end
end
