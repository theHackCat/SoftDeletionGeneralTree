public class FHsdTree<E> implements Cloneable
{
   protected int mSize;
   protected FHsdTreeNode<E> mRoot;
   
   public FHsdTree() { clear(); }
   public boolean empty() { return (mSize == 0); }
   public int size() { return size(mRoot, 0); };
   public int sizePhysical() {
	   return mSize;
   }
   
   public boolean collectGarbage() {
	   return collectGarbage(mRoot, 0);
   }
   
   public void clear() { 
	   mSize = 0; mRoot = null;
   }
   
   public FHsdTreeNode<E> find(E x) { return find(mRoot, x, 0); }
   public boolean remove(E x) { return remove(mRoot, x); }
   public void display() { display(mRoot, 0); };
   public void  displayPhysical()  { displayPhysical(mRoot, 0); }
   
   public < F extends Traverser< ? super E > > 
   void traverse(F func)  { traverse(func, mRoot, 0); }
   
   public FHsdTreeNode<E> addChild( FHsdTreeNode<E> treeNode,  E x )
   {
  
      if (mSize == 0)
      {
         if (treeNode != null)
            return null; 
         mRoot = new FHsdTreeNode<E>(x, null, null, null);
         mRoot.myRoot = mRoot;
         mSize = 1;
         return mRoot;
      }
      if (treeNode == null)
         return null; 
      if (treeNode.myRoot != mRoot)
         return null;  
      if(treeNode.deleted)
    	  return null;

      
      FHsdTreeNode<E> newNode = new FHsdTreeNode<E>(x, 
         treeNode.firstChild, null, treeNode, mRoot);  // sb, chld, prv, rt
      treeNode.firstChild = newNode;
      if (newNode.sib != null)
         newNode.sib.prev = newNode;
      ++mSize;
      return newNode;  
   }
   
   public FHsdTreeNode<E> find(FHsdTreeNode<E> root, E x, int level)
   {
      FHsdTreeNode<E> retval;

      if (mSize == 0 || root == null)
         return null;
      
      if(root.deleted)
    	  return null;

      if (root.data.equals(x) && !root.deleted)
         return root;

      if ( level > 0 && (retval = find(root.sib, x, level)) != null )
         return retval;
      return find(root.firstChild, x, ++level);
   }
   
   public boolean remove(FHsdTreeNode<E> root, E x)
   {
      FHsdTreeNode<E> tn = null;

      if (mSize == 0 || root == null)
         return false;
     
      if ( (tn = find(root, x, 0)) != null )
      {
         removeNode(tn);
         mSize--;
         tn.deleted = true;
         return true;
      }
      return false;
   }
   
   private void removeNode(FHsdTreeNode<E> nodeToDelete )
   {
      if (nodeToDelete == null || mRoot == null)
         return;
      if (nodeToDelete.myRoot != mRoot)
         return;  

      while (nodeToDelete.firstChild != null)
         removeNode(nodeToDelete.firstChild);

      if (nodeToDelete.prev == null)
         mRoot = null;  
      else if (nodeToDelete.prev.sib == nodeToDelete)
         nodeToDelete.prev.sib = nodeToDelete.sib; 
      else
         nodeToDelete.prev.firstChild = nodeToDelete.sib;  

      if (nodeToDelete.sib != null)
         nodeToDelete.sib.prev = nodeToDelete.prev;
      
      mSize--;
   }
   
   public boolean collectGarbage(FHsdTreeNode<E> node, int level) {
	   
	   boolean dltd = false;
	   
	   for(FHsdTreeNode<E> temp = node.firstChild; temp != null; temp = temp.sib) {
		   if(temp.deleted) {
			   removeNode(temp);
			   dltd = true;
		   }
		   
		   collectGarbage(temp, level + 1);
	   }
	   
	   return dltd;
   }
   
   public Object clone() throws CloneNotSupportedException
   {
      FHsdTree<E> newObject = (FHsdTree<E>)super.clone();
      newObject.clear();  

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;
      newObject.setMyRoots(newObject.mRoot);
      
      return newObject;
   }
   
   private FHsdTreeNode<E> cloneSubtree(FHsdTreeNode<E> root)
   {
      FHsdTreeNode<E> newNode;
      if (root == null)
         return null;

      newNode = new FHsdTreeNode<E>
      (
         root.data, 
         cloneSubtree(root.sib), cloneSubtree(root.firstChild),
         null
      );
      
      if (newNode.sib != null)
         newNode.sib.prev = newNode;
      if (newNode.firstChild != null)
         newNode.firstChild.prev = newNode;
      return newNode;
   }
   
   private void setMyRoots(FHsdTreeNode<E> treeNode)
   {
      if (treeNode == null)
         return;

      treeNode.myRoot = mRoot;
      setMyRoots(treeNode.sib);
      setMyRoots(treeNode.firstChild);
   }

   final static String blankString = "                                    ";

   public void  display(FHsdTreeNode<E> treeNode, int level) 
   {
      String indent;

      if(treeNode == null || treeNode.deleted)
    	  return;
      
      if  (level > (int)blankString.length() - 1)
      {
         System.out.println( blankString + " ... " );
         return;
      }
      
      if (treeNode == null)
         return;

      indent = blankString.substring(0, level);

      if(!treeNode.deleted)
    	  System.out.println( indent + treeNode.data ) ;

      for(FHsdTreeNode<E> temp = treeNode.firstChild; temp != null; temp = temp.sib)
    	  display(temp, level + 1);
   }
      
   public <F extends Traverser<? super E>> 
   void traverse(F func, FHsdTreeNode<E> treeNode, int level)
   {
      if (treeNode == null)
         return;

      func.visit(treeNode.data);
 
      traverse( func, treeNode.firstChild, level + 1);
      if (level > 0 )
         traverse( func,  treeNode.sib, level);
   }
   public int sizePhysical(FHsdTreeNode<E> treeNode, int level, int counter) { 
	   if(treeNode == null)
		   return 0;
	   if(!treeNode.deleted) {
		   counter++;
		   counter = sizePhysical(treeNode.firstChild, level + 1, counter);
	   }
	   if(level > 0) {
		   counter = sizePhysical(treeNode.sib, level, counter);
	   }
	   return counter;
   }
   
   protected void displayPhysical(FHsdTreeNode<E> treeNode, int level) {
	   String indent;
	   
	   if(treeNode == null)
		   return;
	   if(level > (int)blankString.length() - 1) {
		   System.out.println(blankString + "...");
		   return;
	   }
	   indent = blankString.substring(0, level);
	   
	   System.out.print(indent+treeNode.data);
	   if(treeNode.deleted)
		   System.out.print(" (D) ");
	   System.out.println();
	   
	   for(FHsdTreeNode<E> temp = treeNode.firstChild; temp != null; temp = temp.sib)
		   displayPhysical(temp, level + 1);
   }
   
   protected int size(FHsdTreeNode<E> treeNode, int level) {
	   int x = 0;
	   
	   if(level == 0)
		   x = 1;
	   
	   if(treeNode == null || treeNode.deleted)
		   return 0;
	   
	   for(FHsdTreeNode<E> temp = treeNode.firstChild; temp != null; temp = temp.sib) {
		   x++;
		   size(temp, level + 1);
	   }
	   
	   return x;
   }
}

interface Traverser<E>
{
   public void visit(E x);
}