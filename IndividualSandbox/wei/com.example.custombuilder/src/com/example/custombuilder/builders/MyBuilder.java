package com.example.custombuilder.builders;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public class MyBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "com.example.custombuilder.myBuilder";

	@Override
	protected IProject[] build(final int kind, final Map<String, String> args, final IProgressMonitor monitor)
			throws CoreException {

		System.out.println("Custom builder triggered");

		// get the project to build
		getProject();

		switch (kind) {

		case FULL_BUILD:
			break;

		case INCREMENTAL_BUILD:
			break;

		case AUTO_BUILD:
			break;
		}

		return null;
	}
}
