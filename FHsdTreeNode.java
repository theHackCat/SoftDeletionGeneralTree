public class FHsdTreeNode<E>
{

   protected FHsdTreeNode<E> firstChild, sib, prev;
   protected E data;
   protected FHsdTreeNode<E> myRoot;  
   protected boolean deleted;

   public FHsdTreeNode( E d, FHsdTreeNode<E> sb, FHsdTreeNode<E> chld, FHsdTreeNode<E> prv)
   {
      firstChild = chld; 
      sib = sb;
      prev = prv;
      data = d;
      myRoot = null;
      deleted = false;
   }
   
   public FHsdTreeNode()
   {
      this(null, null, null, null);
   }
   
   public E getData() { return data; }

   protected FHsdTreeNode( E d, FHsdTreeNode<E> sb, FHsdTreeNode<E> chld, 
      FHsdTreeNode<E> prv, FHsdTreeNode<E> root)
   {
      this(d, sb, chld, prv);
      myRoot = root;
   }
}
