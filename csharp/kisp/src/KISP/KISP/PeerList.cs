using System;
using System.Collections;
using System.Text;
using System.Net;

namespace KISP.Nexus
{
    /// <summary>
    /// List of Peer objects
    /// </summary>
    public class PeerList
    {
        private ArrayList _PeerList;

        /// <summary>
        /// Create new PeerList
        /// </summary>
        public PeerList()
        {
            _PeerList = new ArrayList();
        }

        /// <summary>
        /// Delete peer from list.
        /// </summary>
        /// <param name="IPAddress"></param>
        public void DeletePeer(string IPAddress)
        {
            for (int i = 1; i <= _PeerList.Count; i++)
            {
                if (((Peer)_PeerList[i -1]).IPAddr.ToString() == IPAddress)
                {
                    _PeerList.RemoveAt(i - 1);
                    return;
                }
            }
        }

        /// <summary>
        /// Add a new Peer to the system
        /// </summary>
        /// <param name="IPAddr">The IP address of the peer</param>
        public void NewPeer(IPAddress IPAddr)
        {
            Peer NewPeer = new Peer(IPAddr);
            _PeerList.Add(NewPeer);
        }

        /// <summary>
        /// Set user name of peer
        /// </summary>
        /// <param name="IPAddr">IP address of peer as a string</param>
        /// <param name="Name">user name of peer</param>
        public void SetPeerName(string IPAddr, string Name)
        {
            foreach (Peer CurrentPeer in _PeerList)
                if (CurrentPeer.IPAddr.ToString() == IPAddr)
                {
                    CurrentPeer.UserName = Name;
                    break;
                }
        }

        /// <summary>
        /// Get array list so that it's easy to iterate thru all the Peer objects in it
        /// </summary>
        public ArrayList Peers
        {
            get
            {
                return _PeerList;
            }
        }

        /// <summary>
        /// Get the index for the peer's name
        /// </summary>
        /// <param name="Name">Name for the peer</param>
        /// <returns>integer index</returns>
        public int GetPeerIndex(string Name)
        {
            foreach (Peer p in _PeerList)
                if (p.UserName == Name)
                    return _PeerList.IndexOf(p);
            throw new BullshitException("Peer " + Name + " not found");
        }

        /// <summary>
        /// Get a peer
        /// </summary>
        /// <param name="Name"></param>
        /// <returns></returns>
        public Peer GetPeer(string Name)
        {
            return (Peer) _PeerList[GetPeerIndex(Name)];
        }

        /// <summary>
        /// Get a peer from the list
        /// </summary>
        /// <param name="Index">Index from peer</param>
        /// <returns>returns the peer</returns>
        public Peer GetPeer(int Index)
        {
            return (Peer)_PeerList[Index];
        }

        /// <summary>
        /// See if an IP address is in the list
        /// </summary>
        /// <param name="IPAddr">IP address to search for</param>
        /// <returns>True if IP address is in the list</returns>
        public bool HasPeer(string IPAddr)
        {
            foreach(Peer p in _PeerList)
                if (p.IPAddr.ToString() == IPAddr)
                    return true;
            return false;
        }
    }
}
