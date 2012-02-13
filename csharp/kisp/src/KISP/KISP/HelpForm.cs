using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;

namespace KISP
{
    public partial class HelpForm : Form
    {
        /// <summary>
        /// Constructor for the helpform
        /// </summary>
        public HelpForm()
        {
            InitializeComponent();
        }

        private void HelpForm_Load(object sender, EventArgs e)
        {
            StreamReader HelpReader = new StreamReader("help.dat");
            string Line = HelpReader.ReadLine();

            while (Line != null)
            {
                Listbox_help.Items.Add(Line);
                Line = HelpReader.ReadLine();
            }
            HelpReader.Close();
        }

        private void Btn_Exit_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        private void Btn_Donate_Click(object sender, EventArgs e)
        {
            MessageBox.Show("Please donate us yur d0p3 and n4k3d w0m3n!");
        }
    }
}