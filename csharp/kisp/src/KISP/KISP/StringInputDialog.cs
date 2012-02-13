using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace KISP.Chess
{
    public partial class StringInputDialog : Form
    {
        /// <summary>
        /// The Message that was entered
        /// </summary>
        public string Message = string.Empty;
        /// <summary>
        /// Was the dialogresult ok?
        /// </summary>
        public bool OK = false;
        /// <summary>
        /// Default Form constructor
        /// </summary>
        public StringInputDialog()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Event handler for the Send click button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void BtnSend_Click(object sender, EventArgs e)
        {
            if (InputMessageBox.Text.Length > 0)
            {
                Message = InputMessageBox.Text;
                OK = true;
                this.Hide();
            }
        }

        /// <summary>
        /// Event handler for the Cancel button
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Btn_Cancel_Click(object sender, EventArgs e)
        {
            OK = false;
            this.Hide();
        }
    }
}