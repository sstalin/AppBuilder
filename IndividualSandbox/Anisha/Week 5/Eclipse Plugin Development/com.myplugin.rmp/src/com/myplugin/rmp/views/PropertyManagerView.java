package com.myplugin.rmp.views;
   import java.util.ArrayList;
   import org.eclipse.core.resources.IFile;
   import org.eclipse.core.resources.IFolder;
   import org.eclipse.core.resources.IProject;
   import org.eclipse.core.resources.IResource;
   import org.eclipse.core.resources.IWorkspace;
   import org.eclipse.core.resources.ResourcesPlugin;
   import org.eclipse.core.runtime.IAdaptable;
  import org.eclipse.jface.action.Action;
  import org.eclipse.jface.action.MenuManager;
  import org.eclipse.jface.viewers.DoubleClickEvent;
  import org.eclipse.jface.viewers.IDoubleClickListener;
  import org.eclipse.jface.viewers.ISelection;
  import org.eclipse.jface.viewers.IStructuredSelection;
  import org.eclipse.jface.viewers.ITreeContentProvider;
  import org.eclipse.jface.viewers.LabelProvider;
  import org.eclipse.jface.viewers.TreeViewer;
  import org.eclipse.jface.viewers.Viewer;
  import org.eclipse.swt.SWT;
  import org.eclipse.swt.graphics.Image;
  import org.eclipse.swt.widgets.Composite;
  import org.eclipse.swt.widgets.Menu;
  import org.eclipse.ui.ISharedImages;
  import org.eclipse.ui.IWorkbenchPage;
  import org.eclipse.ui.PlatformUI;
  import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.ViewPart;

  public class PropertyManagerView extends ViewPart {
      private TreeViewer viewer;
     private TreeParent invisibleRoot;
class TreeObject implements IAdaptable {
     private String name;
     private TreeParent parent;
     private IResource resouce;

     public TreeObject(String name) {
               this.name = name;
     }

     public String getName() {
               return name;
     }

     public void setParent(TreeParent parent) {
               this.parent = parent;
     }


     public TreeParent getParent() {
               return parent;
     }
     public String toString() {
               return getName();
     }


     public Object getAdapter(Class key) {
               return null;
     }
     protected IResource getResouce() {
               return resouce;
     }
     protected void setResouce(IResource resouce) {
               this.resouce = resouce;
     }
}
class TreeParent extends TreeObject {
     private ArrayList children;
     public TreeParent(String name) {
               super(name);
               children = new ArrayList();
     }

     public void addChild(TreeObject child) {
               children.add(child);
               child.setParent(this);
     }

     public void removeChild(TreeObject child) {
               children.remove(child);
               child.setParent(null);
     }

     public TreeObject[] getChildren() {
         return (TreeObject[]) children.toArray(new TreeObject[children.size()]);
}

public boolean hasChildren() {
         return children.size() > 0;
}

}
class ViewContentProvider implements ITreeContentProvider {

public void inputChanged(Viewer v, Object oldInput, Object newInput) {
}

public void dispose() {
}

public Object[] getElements(Object parent) {
         if (parent.equals(getViewSite())) {
                  if (invisibleRoot == null)
                            initialize();

                  return getChildren(invisibleRoot);
         }

         return getChildren(parent);
}

public Object getParent(Object child) {
         if (child instanceof TreeObject) {
                  return ((TreeObject) child).getParent();
                 }

                 return null;
       }


public Object[] getChildren(Object parent) {

    if (parent instanceof TreeParent) {
             return ((TreeParent) parent).getChildren();
    }

    return new Object[0];
}

public boolean hasChildren(Object parent) {
    if (parent instanceof TreeParent)
             return ((TreeParent) parent).hasChildren();
    return false;
}

}

class ViewLabelProvider extends LabelProvider {
public String getText(Object obj) {
    return obj.toString();
}
public Image getImage(Object obj) {
String imageKey = ISharedImages.IMG_OBJ_ELEMENT;

if (obj instanceof TreeParent)
 imageKey = ISharedImages.IMG_OBJ_FOLDER;
 return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
}

}

public void initialize() {
TreeParent root = new TreeParent("WorkSpace Property Files");
try {
IWorkspace workspace = ResourcesPlugin.getWorkspace();

IProject[] projects = workspace.getRoot().getProjects();

for (int i = 0; i < projects.length; i++) {
   IResource[] folderResources = projects[i].members();

   for (int j = 0; j < folderResources.length; j++) {

       if (folderResources[j] instanceof IFolder) {
          IFolder resource = (IFolder) folderResources[j];
          if (resource.getName().equalsIgnoreCase("Property Files")) {
             IResource[] fileResources = resource.members();
             for (int k = 0; k < fileResources.length; k++) {
                if (fileResources[k] instanceof IFile &&           
                       fileResources[k].getName().endsWith(".properties")){
                   TreeObject obj = new TreeObject(fileResources[k]
			.getName());
                    obj.setResouce(fileResources[k]);
                    root.addChild(obj);
                 }
              }
        	 }
       }
   }
}
}catch (Exception e) {
// log exception
}
invisibleRoot = new TreeParent("");
invisibleRoot.addChild(root);
}

public PropertyManagerView() {
}

public void createPartControl(Composite parent) {
         viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
         viewer.setContentProvider(new ViewContentProvider());
         viewer.setLabelProvider(new ViewLabelProvider());
         viewer.setInput(getViewSite());
         hookContextMenu();
         hookDoubleCLickAction();
}


private void hookDoubleCLickAction() {
         viewer.addDoubleClickListener(new IDoubleClickListener() {
              public void doubleClick(DoubleClickEvent event) {
                   ISelection selection = event.getSelection();
                   Object obj = ((IStructuredSelection) selection).getFirstElement();
                   if (!(obj instanceof TreeObject)) {
                         return;
                   }else {
                        TreeObject tempObj = (TreeObject) obj;
                        IFile ifile = ResourcesPlugin.getWorkspace().getRoot().
			      getFile(tempObj.getResouce().getFullPath());
                        IWorkbenchPage dpage =
                                           PropertyManagerView.this.getViewSite()
							  .getWorkbenchWindow().getActivePage();
                        if (dpage != null) {
                            try {
                                  IDE.openEditor(dpage, ifile,true);
                            }catch (Exception e) {
                                             // log exception
                         }
                    }
               }
           };
     });
}
private void hookContextMenu() {
         MenuManager menuMgr = new MenuManager("#PopupMenu");
         Menu menu = menuMgr.createContextMenu(viewer.getControl());
         viewer.getControl().setMenu(menu);
         Action refresh =new Action() {
                   public void run() {
                            initialize();
                            viewer.refresh();
                   }
         };
         refresh.setText("Refresh");
         menuMgr.add(refresh);
}

public void setFocus() {
         viewer.getControl().setFocus();
}
}


