using System;
using System.Collections.Generic;
using System.Collections;
using System.Text;
using System.Xml;
using KISP.Nexus;

namespace KISP.Search
{
    /// <summary>
    /// Container class for different types of search methods
    /// </summary>
    public class Search
    {
        private ArrayList Shares = new ArrayList();

        /// <summary>
        /// Performs the search on all search methods
        /// </summary>
        public Search()
        {
            //TODO: Add iShare Implementations to ArrayList Shares

            int i = 0;

            for (i = 0; i < Configuration.ShareCount(); i++)
            {
                switch (Configuration.ShareType(i))
                {
                    case "dir":
                        {
                            Shares.Add(new SearchLocal(Configuration.LocalShareDir(i)));
                            break;
                        }
                }
            }
        }

        /// <summary>
        /// Performs the actual search
        /// </summary>
        /// <param name="SearchString">The string to be searched for</param>
        /// <returns>XMLDocument with the search results</returns>
        public XmlDocument Find(string SearchString)
        {
            XmlDocument SearchDocument = new XmlDocument();
            SearchDocument.AppendChild(SearchDocument.CreateElement("searchresults"));
            foreach (iShare CurrentShare in Shares)
            {
                SearchDocument = CurrentShare.Find(SearchString, SearchDocument);
            }

            return SearchDocument;
        }
    }
}
