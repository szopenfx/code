namespace KISP.Chess
{
    partial class ChessForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ChessForm));
            this.ChessPanel = new System.Windows.Forms.Panel();
            this.Image_Pieces = new System.Windows.Forms.ImageList(this.components);
            this.ListBox_History = new System.Windows.Forms.ListBox();
            this.Btn_OfferDraw = new System.Windows.Forms.Button();
            this.Btn_Surrender = new System.Windows.Forms.Button();
            this.Btn_Quit = new System.Windows.Forms.Button();
            this.Btn_SendMessage = new System.Windows.Forms.Button();
            this.GrpBox_Control = new System.Windows.Forms.GroupBox();
            this.label3 = new System.Windows.Forms.Label();
            this.Combo_Promotion = new System.Windows.Forms.ComboBox();
            this.Grp_History = new System.Windows.Forms.GroupBox();
            this.Lbl_MoveTime = new System.Windows.Forms.Label();
            this.LblMoveTime = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.Lbl_TotalTime = new System.Windows.Forms.Label();
            this.MainTimer = new System.Windows.Forms.Timer(this.components);
            this.label4 = new System.Windows.Forms.Label();
            this.Lbl_MyColor = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.Lbl_PTM = new System.Windows.Forms.PictureBox();
            this.GrpBox_Control.SuspendLayout();
            this.Grp_History.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.Lbl_PTM)).BeginInit();
            this.SuspendLayout();
            // 
            // ChessPanel
            // 
            this.ChessPanel.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.ChessPanel.BackColor = System.Drawing.Color.Transparent;
            this.ChessPanel.Location = new System.Drawing.Point(12, 12);
            this.ChessPanel.Name = "ChessPanel";
            this.ChessPanel.Size = new System.Drawing.Size(429, 490);
            this.ChessPanel.TabIndex = 0;
            // 
            // Image_Pieces
            // 
            this.Image_Pieces.ImageStream = ((System.Windows.Forms.ImageListStreamer)(resources.GetObject("Image_Pieces.ImageStream")));
            this.Image_Pieces.TransparentColor = System.Drawing.Color.Red;
            this.Image_Pieces.Images.SetKeyName(0, "black_pawn.png");
            this.Image_Pieces.Images.SetKeyName(1, "black_bishop.png");
            this.Image_Pieces.Images.SetKeyName(2, "black_king.png");
            this.Image_Pieces.Images.SetKeyName(3, "black_knight.png");
            this.Image_Pieces.Images.SetKeyName(4, "black_queen.png");
            this.Image_Pieces.Images.SetKeyName(5, "black_rook.png");
            this.Image_Pieces.Images.SetKeyName(6, "white_pawn.png");
            this.Image_Pieces.Images.SetKeyName(7, "white_bishop.png");
            this.Image_Pieces.Images.SetKeyName(8, "black_king.png");
            this.Image_Pieces.Images.SetKeyName(9, "white_knight.png");
            this.Image_Pieces.Images.SetKeyName(10, "white_queen.png");
            this.Image_Pieces.Images.SetKeyName(11, "white_rook.png");
            // 
            // ListBox_History
            // 
            this.ListBox_History.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.ListBox_History.FormattingEnabled = true;
            this.ListBox_History.Location = new System.Drawing.Point(4, 105);
            this.ListBox_History.Name = "ListBox_History";
            this.ListBox_History.Size = new System.Drawing.Size(120, 82);
            this.ListBox_History.TabIndex = 1;
            // 
            // Btn_OfferDraw
            // 
            this.Btn_OfferDraw.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.Btn_OfferDraw.Location = new System.Drawing.Point(6, 55);
            this.Btn_OfferDraw.Name = "Btn_OfferDraw";
            this.Btn_OfferDraw.Size = new System.Drawing.Size(120, 26);
            this.Btn_OfferDraw.TabIndex = 2;
            this.Btn_OfferDraw.Text = "Offer Draw";
            this.Btn_OfferDraw.UseVisualStyleBackColor = true;
            this.Btn_OfferDraw.Click += new System.EventHandler(this.Btn_OfferDraw_Click);
            // 
            // Btn_Surrender
            // 
            this.Btn_Surrender.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.Btn_Surrender.Location = new System.Drawing.Point(6, 87);
            this.Btn_Surrender.Name = "Btn_Surrender";
            this.Btn_Surrender.Size = new System.Drawing.Size(120, 23);
            this.Btn_Surrender.TabIndex = 3;
            this.Btn_Surrender.Text = "Surrender";
            this.Btn_Surrender.UseVisualStyleBackColor = true;
            this.Btn_Surrender.Click += new System.EventHandler(this.Btn_Surrender_Click);
            // 
            // Btn_Quit
            // 
            this.Btn_Quit.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.Btn_Quit.Location = new System.Drawing.Point(6, 116);
            this.Btn_Quit.Name = "Btn_Quit";
            this.Btn_Quit.Size = new System.Drawing.Size(120, 23);
            this.Btn_Quit.TabIndex = 4;
            this.Btn_Quit.Text = "Quit";
            this.Btn_Quit.UseVisualStyleBackColor = true;
            this.Btn_Quit.Click += new System.EventHandler(this.Btn_Quit_Click);
            // 
            // Btn_SendMessage
            // 
            this.Btn_SendMessage.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.Btn_SendMessage.Location = new System.Drawing.Point(9, 19);
            this.Btn_SendMessage.Name = "Btn_SendMessage";
            this.Btn_SendMessage.Size = new System.Drawing.Size(117, 23);
            this.Btn_SendMessage.TabIndex = 5;
            this.Btn_SendMessage.Text = "Send Message";
            this.Btn_SendMessage.UseVisualStyleBackColor = true;
            this.Btn_SendMessage.Click += new System.EventHandler(this.Btn_SendMessage_Click);
            // 
            // GrpBox_Control
            // 
            this.GrpBox_Control.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.GrpBox_Control.Controls.Add(this.label3);
            this.GrpBox_Control.Controls.Add(this.Combo_Promotion);
            this.GrpBox_Control.Controls.Add(this.Btn_SendMessage);
            this.GrpBox_Control.Controls.Add(this.Btn_Quit);
            this.GrpBox_Control.Controls.Add(this.Btn_Surrender);
            this.GrpBox_Control.Controls.Add(this.Btn_OfferDraw);
            this.GrpBox_Control.Location = new System.Drawing.Point(451, 297);
            this.GrpBox_Control.Name = "GrpBox_Control";
            this.GrpBox_Control.Size = new System.Drawing.Size(132, 205);
            this.GrpBox_Control.TabIndex = 6;
            this.GrpBox_Control.TabStop = false;
            this.GrpBox_Control.Text = "Control";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(3, 162);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(113, 13);
            this.label3.TabIndex = 7;
            this.label3.Text = "Default promotion type";
            // 
            // Combo_Promotion
            // 
            this.Combo_Promotion.FormattingEnabled = true;
            this.Combo_Promotion.Items.AddRange(new object[] {
            "Queen",
            "Bishop",
            "Rook",
            "Knight"});
            this.Combo_Promotion.Location = new System.Drawing.Point(3, 178);
            this.Combo_Promotion.Name = "Combo_Promotion";
            this.Combo_Promotion.Size = new System.Drawing.Size(121, 21);
            this.Combo_Promotion.TabIndex = 6;
            this.Combo_Promotion.Text = "Queen";
            // 
            // Grp_History
            // 
            this.Grp_History.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.Grp_History.Controls.Add(this.Lbl_PTM);
            this.Grp_History.Controls.Add(this.label5);
            this.Grp_History.Controls.Add(this.Lbl_MyColor);
            this.Grp_History.Controls.Add(this.label4);
            this.Grp_History.Controls.Add(this.Lbl_MoveTime);
            this.Grp_History.Controls.Add(this.LblMoveTime);
            this.Grp_History.Controls.Add(this.label2);
            this.Grp_History.Controls.Add(this.label1);
            this.Grp_History.Controls.Add(this.Lbl_TotalTime);
            this.Grp_History.Controls.Add(this.ListBox_History);
            this.Grp_History.Location = new System.Drawing.Point(451, 13);
            this.Grp_History.Name = "Grp_History";
            this.Grp_History.Size = new System.Drawing.Size(131, 278);
            this.Grp_History.TabIndex = 7;
            this.Grp_History.TabStop = false;
            this.Grp_History.Text = "History";
            // 
            // Lbl_MoveTime
            // 
            this.Lbl_MoveTime.AutoSize = true;
            this.Lbl_MoveTime.Location = new System.Drawing.Point(78, 40);
            this.Lbl_MoveTime.Name = "Lbl_MoveTime";
            this.Lbl_MoveTime.Size = new System.Drawing.Size(49, 13);
            this.Lbl_MoveTime.TabIndex = 6;
            this.Lbl_MoveTime.Text = "00:00:00";
            // 
            // LblMoveTime
            // 
            this.LblMoveTime.AutoSize = true;
            this.LblMoveTime.Location = new System.Drawing.Point(7, 40);
            this.LblMoveTime.Name = "LblMoveTime";
            this.LblMoveTime.Size = new System.Drawing.Size(56, 13);
            this.LblMoveTime.TabIndex = 5;
            this.LblMoveTime.Text = "Move time";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(1, 89);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(67, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Move history";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(7, 16);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(53, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Total time";
            // 
            // Lbl_TotalTime
            // 
            this.Lbl_TotalTime.AutoSize = true;
            this.Lbl_TotalTime.Location = new System.Drawing.Point(78, 16);
            this.Lbl_TotalTime.Name = "Lbl_TotalTime";
            this.Lbl_TotalTime.Size = new System.Drawing.Size(49, 13);
            this.Lbl_TotalTime.TabIndex = 2;
            this.Lbl_TotalTime.Text = "00:00:00";
            // 
            // MainTimer
            // 
            this.MainTimer.Interval = 1000;
            this.MainTimer.Tick += new System.EventHandler(this.MainTimer_Elapsed);
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(7, 64);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(56, 13);
            this.label4.TabIndex = 7;
            this.label4.Text = "Your Color";
            // 
            // Lbl_MyColor
            // 
            this.Lbl_MyColor.AutoSize = true;
            this.Lbl_MyColor.Location = new System.Drawing.Point(78, 64);
            this.Lbl_MyColor.Name = "Lbl_MyColor";
            this.Lbl_MyColor.Size = new System.Drawing.Size(22, 13);
            this.Lbl_MyColor.TabIndex = 8;
            this.Lbl_MyColor.Text = "xxx";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(7, 190);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(77, 13);
            this.label5.TabIndex = 9;
            this.label5.Text = "Player to move";
            // 
            // Lbl_PTM
            // 
            this.Lbl_PTM.Location = new System.Drawing.Point(16, 206);
            this.Lbl_PTM.Name = "Lbl_PTM";
            this.Lbl_PTM.Size = new System.Drawing.Size(100, 66);
            this.Lbl_PTM.TabIndex = 10;
            this.Lbl_PTM.TabStop = false;
            // 
            // ChessForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(595, 525);
            this.Controls.Add(this.Grp_History);
            this.Controls.Add(this.GrpBox_Control);
            this.Controls.Add(this.ChessPanel);
            this.MaximumSize = new System.Drawing.Size(1000, 800);
            this.Name = "ChessForm";
            this.Text = "Chess Game";
            this.Load += new System.EventHandler(this.ChessForm_Load);
            this.GrpBox_Control.ResumeLayout(false);
            this.GrpBox_Control.PerformLayout();
            this.Grp_History.ResumeLayout(false);
            this.Grp_History.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.Lbl_PTM)).EndInit();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Panel ChessPanel;
        private System.Windows.Forms.ImageList Image_Pieces;
        private System.Windows.Forms.ListBox ListBox_History;
        private System.Windows.Forms.Button Btn_OfferDraw;
        private System.Windows.Forms.Button Btn_Surrender;
        private System.Windows.Forms.Button Btn_Quit;
        private System.Windows.Forms.Button Btn_SendMessage;
        private System.Windows.Forms.GroupBox GrpBox_Control;
        private System.Windows.Forms.GroupBox Grp_History;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label Lbl_TotalTime;
        private System.Windows.Forms.Timer MainTimer;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label Lbl_MoveTime;
        private System.Windows.Forms.Label LblMoveTime;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.ComboBox Combo_Promotion;
        private System.Windows.Forms.Label Lbl_MyColor;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.PictureBox Lbl_PTM;
    }
}