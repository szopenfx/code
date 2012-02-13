using System;
using System.Collections.Generic;
using System.Text;
using QuartzTypeLib;
using System.IO;

namespace KISP.Interface.Graphical
{
    partial class GraphicalInterface
    {
        private const int WM_APP = 0x8000;
        private const int WM_GRAPHNOTIFY = WM_APP + 1;
        private const int EC_COMPLETE = 0x01;
        private const int WS_CHILD = 0x40000000;
        private const int WS_CLIPCHILDREN = 0x2000000;

        private FilgraphManager m_objFilterGraph = null;
        private IBasicAudio m_objBasicAudio = null;
        private IVideoWindow m_objVideoWindow = null;
        private IMediaEvent m_objMediaEvent = null;
        private IMediaEventEx m_objMediaEventEx = null;
        private IMediaPosition m_objMediaPosition = null;
        private IMediaControl m_objMediaControl = null;



        enum MediaStatus { None, Stopped, Paused, Running };

        private MediaStatus m_CurrentStatus = MediaStatus.None;

        /// <summary>
        /// Plays the given mediafile in the internal mediaplayer
        /// </summary>
        /// <param name="FilePath">File to play</param>
        public void ShowMedia(string FilePath)
        {
            StopMedia();
            Lbl_CurrentMedia.Text = Path.GetFileName(FilePath);
            m_objFilterGraph = new FilgraphManager();
            m_objFilterGraph.RenderFile(FilePath);

            m_objBasicAudio = m_objFilterGraph as IBasicAudio;

            try
            {
                m_objVideoWindow = m_objFilterGraph as IVideoWindow;
                m_objVideoWindow.Owner = (int)panel2.Handle;
                //m_objVideoWindow.Owner = (int)panel1.Handle;

                m_objVideoWindow.WindowStyle = WS_CHILD | WS_CLIPCHILDREN;
                m_objVideoWindow.SetWindowPosition(panel2.ClientRectangle.Left,
                   panel2.ClientRectangle.Top,
                    panel2.ClientRectangle.Width,
                    panel2.ClientRectangle.Height);
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                m_objVideoWindow = null;
            }

            m_objMediaEvent = m_objFilterGraph as IMediaEvent;

            m_objMediaEventEx = m_objFilterGraph as IMediaEventEx;
            m_objMediaEventEx.SetNotifyWindow((int)this.Handle, WM_GRAPHNOTIFY, 0);

            m_objMediaPosition = m_objFilterGraph as IMediaPosition;

            m_objMediaControl = m_objFilterGraph as IMediaControl;

            m_objMediaControl.Run();
            m_CurrentStatus = MediaStatus.Running;
            UpdateMediaButtons();

            //UpdateStatusBar();
            //UpdateToolBar();


        }

        private void GraphicalInterface_SizeChanged(object sender, EventArgs e)
        {
            
        }


        private void Btn_Play_Click(object sender, EventArgs e)
        {
            try
            {
                m_objMediaControl.Run();
                m_CurrentStatus = MediaStatus.Running;
                UpdateMediaButtons();
            }
            catch
            {

            }
        }

        private void Btn_Pause_Click(object sender, EventArgs e)
        {
            m_objMediaControl.Pause();
            m_CurrentStatus = MediaStatus.Paused;
            UpdateMediaButtons();
        }

        private void Btn_Stop_Click(object sender, EventArgs e)
        {
            m_objMediaControl.Stop();
            StopMedia();
            m_CurrentStatus = MediaStatus.Stopped;
            UpdateMediaButtons();
        }

        private void UpdateMediaButtons()
        {
            switch (m_CurrentStatus)
            {
                case MediaStatus.None:
                    {
                        MediaPlayer_ContextMenu.Items["MenuItem_Play"].Enabled = true;
                        MediaPlayer_ContextMenu.Items["MenuItem_Stop"].Enabled = false;
                        MediaPlayer_ContextMenu.Items["MenuItem_Pause"].Enabled = false;
                        break;
                    }
                case MediaStatus.Running:
                    {
                        MediaPlayer_ContextMenu.Items["MenuItem_Play"].Enabled = false;
                        MediaPlayer_ContextMenu.Items["MenuItem_Stop"].Enabled = true;
                        MediaPlayer_ContextMenu.Items["MenuItem_Pause"].Enabled = true;
                        break;
                    }
                case MediaStatus.Paused:
                    {
                        MediaPlayer_ContextMenu.Items["MenuItem_Play"].Enabled = true;
                        MediaPlayer_ContextMenu.Items["MenuItem_Stop"].Enabled = true;
                        MediaPlayer_ContextMenu.Items["MenuItem_Pause"].Enabled = false;
                        break;
                    }
                case MediaStatus.Stopped:
                    {
                        MediaPlayer_ContextMenu.Items["MenuItem_Play"].Enabled = true;
                        MediaPlayer_ContextMenu.Items["MenuItem_Stop"].Enabled = false;
                        MediaPlayer_ContextMenu.Items["MenuItem_Pause"].Enabled = false;
                        break;
                    }
            }
        }

        private void StopMedia()
        {
            if (m_objMediaControl != null)
                m_objMediaControl.Stop();

            m_CurrentStatus = MediaStatus.Stopped;

            if (m_objMediaEventEx != null)
                m_objMediaEventEx.SetNotifyWindow(0, 0, 0);

            if (m_objVideoWindow != null)
            {
                m_objVideoWindow.Visible = 0;
                m_objVideoWindow.Owner = 0;
            }
            m_objFilterGraph = null;
            m_objBasicAudio = null;
            m_objVideoWindow = null;
            m_objMediaEvent = null;
            m_objMediaEventEx = null;
            m_objMediaPosition = null;
            m_objMediaControl = null;

            Lbl_CurrentMedia.Text = "No Media Selected";
        }


    }
}
