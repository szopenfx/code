using System;
using System.Collections.Generic;
using System.Text;

namespace KISP.Interface
{
    public interface IInterface
    {
        void AddUser(string username);
        void DelUser(string username);
        void ChatMessage(string message);
        void DownloadStart(string filename);
        void DownloadEnd(string filename);
        void DisplaySearchResult(string FileName, string Size, string RemoteUserName, string RemoteSource, string FileType);
    }
}
