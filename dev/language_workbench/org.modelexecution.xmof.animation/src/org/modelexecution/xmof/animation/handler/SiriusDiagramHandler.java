package org.modelexecution.xmof.animation.handler;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.sirius.business.api.dialect.command.RefreshRepresentationsCommand;
import org.eclipse.sirius.business.api.session.Session;
import org.eclipse.sirius.business.api.session.SessionManager;
import org.eclipse.sirius.ui.business.api.dialect.DialectEditor;
import org.eclipse.sirius.ui.business.api.dialect.DialectUIManager;
import org.eclipse.sirius.ui.business.api.session.IEditingSession;
import org.eclipse.sirius.ui.business.api.session.SessionUIManager;
import org.eclipse.sirius.viewpoint.DAnalysis;
import org.eclipse.sirius.viewpoint.DRepresentation;
import org.eclipse.sirius.viewpoint.DView;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.gemoc.executionframework.engine.core.CommandExecution;
import org.gemoc.executionframework.extensions.sirius.modelloader.DebugPermissionProvider;
import org.modelexecution.xmof.Syntax.Activities.IntermediateActivities.Activity;

public class SiriusDiagramHandler implements DiagramHandler {

	private Map<String, IEditorPart> diagramEditorMap = new HashMap<>();
	private URI airdURI;

	public SiriusDiagramHandler(URI airdURI) {
		this.airdURI = airdURI;
		killPreviousSiriusSession(airdURI);
		SessionManager.INSTANCE.getSession(airdURI, new NullProgressMonitor());
	}

	private void killPreviousSiriusSession(URI sessionResourceURI) {
		final Session session = SessionManager.INSTANCE.getExistingSession(sessionResourceURI);
		if (session != null) {
			final IEditingSession uiSession = SessionUIManager.INSTANCE.getUISession(session);

			DebugPermissionProvider permProvider = new DebugPermissionProvider();
			if (!permProvider.provides(session.getTransactionalEditingDomain().getResourceSet())) {
				// this is a not debugSession (ie. a normal editing session)
				if (uiSession != null) {
					for (final DialectEditor editor : uiSession.getEditors()) {
						final IEditorSite editorSite = editor.getEditorSite();
						if (editor.getSite() == null) {
							editorSite.getShell().getDisplay().syncExec(new Runnable() {
								@Override
								public void run() {
									editorSite.getPage().closeEditor(editor, true);
								}
							});
						}

					}
					PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

						@Override
						public void run() {
							uiSession.close();
						}
					});
				}
			}
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					session.close(new NullProgressMonitor());
					SessionManager.INSTANCE.remove(session);
				}
			});
		}
	}

	@Override
	public void openOrShowDiagram(Activity activity) {
		String key = activity.getName().toUpperCase();
		if (diagramEditorMap.containsKey(key)) {
			activateDiagramEditor(key);
		} else {
			openDiagramEditor(key);
		}
	}

	public void refreshDiagram(String diagramKey) {
		String key = diagramKey.trim().toUpperCase();
		final Session session = SessionManager.INSTANCE.getExistingSession(airdURI);
		if (session == null || !diagramEditorMap.containsKey(key))
			return;
		DialectEditor editor = (DialectEditor) diagramEditorMap.get(key);

		Collection<DRepresentation> representations = Arrays.asList(editor.getRepresentation());
		TransactionalEditingDomain editingDomain = session.getTransactionalEditingDomain();

		final RefreshRepresentationsCommand refresh = new RefreshRepresentationsCommand(editingDomain,
				new NullProgressMonitor(), representations);
		CommandExecution.execute(editingDomain, refresh);

	}

	private void openDiagramEditor(String key) {
		Session siriusSession = SessionManager.INSTANCE.getExistingSession(airdURI);
		if (siriusSession == null) {
			siriusSession = SessionManager.INSTANCE.getSession(airdURI, new NullProgressMonitor());
		}
		DAnalysis root = (DAnalysis) siriusSession.getSessionResource().getContents().get(0);
		DView dView = root.getOwnedViews().get(0);
		for (DRepresentation representation : dView.getOwnedRepresentations()) {
			if (representation.getName().toUpperCase().contains(key)) {
				IEditorPart editor = DialectUIManager.INSTANCE.openEditor(siriusSession, representation,
						new NullProgressMonitor());
				diagramEditorMap.put(key, editor);
				return;
			}
		}

	}

	private void activateDiagramEditor(String key) {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IEditorPart editor = diagramEditorMap.get(key);
				page.activate(editor);

			}
		});

	}

}
