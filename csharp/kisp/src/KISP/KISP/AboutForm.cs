using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;

namespace KISP
{
    public partial class AboutForm : Form
    {
        /// <summary>
        /// Construct About form
        /// </summary>
        public AboutForm()
        {
            InitializeComponent();
        }

        /// <summary>
        /// Event: button close clicked
        /// </summary>
        /// <param name="sender">Sending object</param>
        /// <param name="e">Event information</param>
        private void Btn_Close_Click(object sender, EventArgs e)
        {
            this.Close();
        }
    }
}