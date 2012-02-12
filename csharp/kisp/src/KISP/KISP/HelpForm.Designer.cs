namespace KISP
{
    /// <summary>
    /// Form to display the helpfile
    /// </summary>
    partial class HelpForm
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
            this.Btn_Exit = new System.Windows.Forms.Button();
            this.Listbox_help = new System.Windows.Forms.ListBox();
            this.Btn_Donate = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // Btn_Exit
            // 
            this.Btn_Exit.Location = new System.Drawing.Point(12, 342);
            this.Btn_Exit.Name = "Btn_Exit";
            this.Btn_Exit.Size = new System.Drawing.Size(75, 23);
            this.Btn_Exit.TabIndex = 0;
            this.Btn_Exit.Text = "Exit";
            this.Btn_Exit.UseVisualStyleBackColor = true;
            this.Btn_Exit.Click += new System.EventHandler(this.Btn_Exit_Click);
            // 
            // Listbox_help
            // 
            this.Listbox_help.FormattingEnabled = true;
            this.Listbox_help.Location = new System.Drawing.Point(12, 12);
            this.Listbox_help.Name = "Listbox_help";
            this.Listbox_help.Size = new System.Drawing.Size(460, 316);
            this.Listbox_help.TabIndex = 1;
            // 
            // Btn_Donate
            // 
            this.Btn_Donate.Location = new System.Drawing.Point(386, 342);
            this.Btn_Donate.Name = "Btn_Donate";
            this.Btn_Donate.Size = new System.Drawing.Size(86, 23);
            this.Btn_Donate.TabIndex = 2;
            this.Btn_Donate.Text = "Donate";
            this.Btn_Donate.UseVisualStyleBackColor = true;
            this.Btn_Donate.Click += new System.EventHandler(this.Btn_Donate_Click);
            // 
            // HelpForm
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(484, 377);
            this.Controls.Add(this.Btn_Donate);
            this.Controls.Add(this.Listbox_help);
            this.Controls.Add(this.Btn_Exit);
            this.Name = "HelpForm";
            this.Text = "Help";
            this.Load += new System.EventHandler(this.HelpForm_Load);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.Button Btn_Exit;
        private System.Windows.Forms.ListBox Listbox_help;
        private System.Windows.Forms.Button Btn_Donate;
    }
}