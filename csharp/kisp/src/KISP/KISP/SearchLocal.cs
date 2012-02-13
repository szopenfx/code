using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Xml;

namespace KISP.Search
{
    /// <summary>
    /// Searches local resources
    /// </summary>
    class SearchLocal : iShare
    {
        DirectoryInfo ShareDirInfo;

        /// <summary>
        /// Sets the default share directory
        /// </summary>
        /// <param name="DirPath">Path to directory to be shared</param>
        public SearchLocal(string DirPath)
        {
            ShareDirInfo = new DirectoryInfo(DirPath);
        }

        /// <summary>
        /// Searches the local directory for a given keyword
        /// </summary>
        /// <param name="SearchString">The keyword to be searched for</param>
        /// <param name="FillinXmlDocument">The XML document to add the search result to</param>
        /// <returns>Returns the new Xml document</returns>
        public XmlDocument Find(string SearchString, XmlDocument FillinXmlDocument)
        {

            foreach (FileInfo CurrFileInfo in ShareDirInfo.GetFiles())
            {
                if (CurrFileInfo.Name.IndexOf(SearchString) > -1)
                {
                    XmlElement TmpElement = FillinXmlDocument.CreateElement("result");

                    // create elements
                    XmlElement TmpSrcElement = FillinXmlDocument.CreateElement("source");
                    XmlElement TmpFilenameElement = FillinXmlDocument.CreateElement("filename");
                    XmlElement TmpSizeElement = FillinXmlDocument.CreateElement("size");
                    //TODO: Get MD5 sum for file

                    // create text nodes
                    XmlText TmpSrcText = FillinXmlDocument.CreateTextNode("LocalShare");
                    XmlText TmpFileNameText = FillinXmlDocument.CreateTextNode(CurrFileInfo.FullName);
                    XmlText TmpSizeText = FillinXmlDocument.CreateTextNode(CurrFileInfo.Length.ToString());

                    // append text nodes
                    TmpSrcElement.AppendChild(TmpSrcText);
                    TmpFilenameElement.AppendChild(TmpFileNameText);
                    TmpSizeElement.AppendChild(TmpSizeText);

                    // append elements
                    TmpElement.AppendChild(TmpSrcElement);
                    TmpElement.AppendChild(TmpFilenameElement);
                    TmpElement.AppendChild(TmpSizeElement);

                    FillinXmlDocument.FirstChild.AppendChild(TmpElement);
                }

            }

            foreach (DirectoryInfo SubDirInfo in ShareDirInfo.GetDirectories())
            {
                foreach (FileInfo CurrFileInfo in SubDirInfo.GetFiles())
                {
                    if (CurrFileInfo.Name.IndexOf(SearchString) > -1)
                    {
                        XmlElement TmpElement = FillinXmlDocument.CreateElement("result");

                        // create elements
                        XmlElement TmpSrcElement = FillinXmlDocument.CreateElement("source");
                        XmlElement TmpFilenameElement = FillinXmlDocument.CreateElement("filename");
                        XmlElement TmpSizeElement = FillinXmlDocument.CreateElement("size");
                        //TODO: Get MD5 sum for file

                        // create text nodes
                        XmlText TmpSrcText = FillinXmlDocument.CreateTextNode("LocalShare");
                        XmlText TmpFileNameText = FillinXmlDocument.CreateTextNode(CurrFileInfo.FullName);
                        XmlText TmpSizeText = FillinXmlDocument.CreateTextNode(CurrFileInfo.Length.ToString());
                        
                        // append text nodes
                        TmpSrcElement.AppendChild(TmpSrcText);
                        TmpFilenameElement.AppendChild(TmpFileNameText);
                        TmpSizeElement.AppendChild(TmpSizeText);

                        // append elements
                        TmpElement.AppendChild(TmpSrcElement);
                        TmpElement.AppendChild(TmpFilenameElement);
                        TmpElement.AppendChild(TmpSizeElement);

                        FillinXmlDocument.FirstChild.AppendChild(TmpElement);
                    }

                }
                if (SubDirInfo.GetDirectories().Length > 0)
                {
                    Find(SearchString, FillinXmlDocument, SubDirInfo.GetDirectories());
                }



            }



            return FillinXmlDocument;
        }


        /// <summary>
        /// Search method, used to find files in subdirectories
        /// </summary>
        /// <param name="SearchString">The string to be searched for</param>
        /// <param name="FillinXmlDocument">The XML document to add the search results to</param>
        /// <param name="SubDirInfoArray">Array with subdirs</param>
        /// <returns>The new XML document with the added changes</returns>
        public XmlDocument Find(string SearchString, XmlDocument FillinXmlDocument, DirectoryInfo[] SubDirInfoArray)
        {
            foreach (DirectoryInfo SubDirInfo in SubDirInfoArray)
            {
                foreach (FileInfo CurrFileInfo in SubDirInfo.GetFiles())
                {
                    if (CurrFileInfo.Name.IndexOf(SearchString) > -1)
                    {
                        XmlElement TmpElement = FillinXmlDocument.CreateElement("result");

                        // create elements
                        XmlElement TmpSrcElement = FillinXmlDocument.CreateElement("source");
                        XmlElement TmpFilenameElement = FillinXmlDocument.CreateElement("filename");
                        XmlElement TmpSizeElement = FillinXmlDocument.CreateElement("size");
                        //TODO: Get MD5 sum for file

                        // create text nodes
                        XmlText TmpSrcText = FillinXmlDocument.CreateTextNode("LocalShare");
                        XmlText TmpFileNameText = FillinXmlDocument.CreateTextNode(CurrFileInfo.FullName);
                        XmlText TmpSizeText = FillinXmlDocument.CreateTextNode(CurrFileInfo.Length.ToString());
                        
                        // append text nodes
                        TmpSrcElement.AppendChild(TmpSrcText);
                        TmpFilenameElement.AppendChild(TmpFileNameText);
                        TmpSizeElement.AppendChild(TmpSizeText);

                        // append elements
                        TmpElement.AppendChild(TmpSrcElement);
                        TmpElement.AppendChild(TmpFilenameElement);
                        TmpElement.AppendChild(TmpSizeElement);

                        FillinXmlDocument.FirstChild.AppendChild(TmpElement);

                    
                    }

                }

                if (SubDirInfo.GetDirectories().Length > 0)
                {
                    Find(SearchString, FillinXmlDocument, SubDirInfo.GetDirectories());
                }


            }



            return FillinXmlDocument;
        }


        

    }
 }
