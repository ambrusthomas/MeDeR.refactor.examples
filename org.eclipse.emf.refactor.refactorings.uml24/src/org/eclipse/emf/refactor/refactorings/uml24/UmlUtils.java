package org.eclipse.emf.refactor.refactorings.uml24;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.Usage;
import org.eclipse.uml2.uml.VisibilityKind;

public abstract class UmlUtils {

	public static boolean haveSameProperties(List<Parameter> parL1, List<Parameter> parL2) { // 23
		EList<Parameter> inputParametersOp1 = getInputParameterList(parL1);
		EList<Parameter> inputParametersOp2 = getInputParameterList(parL2);
		if (inputParametersOp1.size() != inputParametersOp2.size()) return false;
		for (int i=0; i < inputParametersOp1.size(); i++) {
			Parameter par1 = inputParametersOp1.get(i);
			Parameter par2 = inputParametersOp2.get(i);
			if (! (haveSameNames(par1, par2) 
					&& haveSameTypes(par1, par2) 
					&& haveSameDirections(par1, par2) 
					&& haveSameMultiplicities(par1, par2))) {
				return false;
			}
		}
		return true;
	}

	private static boolean haveSameMultiplicities(Parameter par1, Parameter par2) { // 3
		if (par1.getLower() != par2.getLower()) return false;
		if (par1.getUpper() != par2.getUpper()) return false;
		return true;
	}

	private static boolean haveSameDirections(Parameter par1, Parameter par2) { // 1
		return par1.getDirection().equals(par2.getDirection());
	}

	private static boolean haveSameTypes(Parameter par1, Parameter par2) { // 4
		if (par1.getType() == null && par2.getType() == null) return true;
		if (par1.getType() == null && par2.getType() != null) return false;
		if (par1.getType() != null && par2.getType() == null) return false;
		return (par1.getType().equals(par2.getType()));
	}

	private static boolean haveSameNames(Parameter par1, Parameter par2) { // 1
		return par1.getName().equals(par2.getName());
	}

	public static EList<Parameter> getInputParameterList( // 5
			List<Parameter> parameters) {
		EList<Parameter> inputParameterList = new BasicEList<Parameter>();
		for (Parameter param : parameters) {
			if (param.getDirection().getValue() != ParameterDirectionKind.RETURN) {
				inputParameterList.add(param);
			}
		}
		return inputParameterList;
	}

	public static boolean haveSameTypes(Operation op1, Operation op2) { // 4
		if (op1.getType() == null && op2.getType() == null) return true;
		if (op1.getType() == null && op2.getType() != null) return false;
		if (op1.getType() != null && op2.getType() == null) return false;
		return (op1.getType().equals(op2.getType()));
	}

	public static boolean hasAttributes(Class cl) { // 1
		return (! cl.getOwnedAttributes().isEmpty());
	}

	public static boolean hasOperations(Class cl) { // 1
		return (! cl.getOwnedOperations().isEmpty());
	}

	public static boolean hasSuperclasses(Class cl) { // 1
		return (! cl.getSuperClasses().isEmpty());
	}

	public static boolean hasInnerClasses(Class cl) { // 1
		return (! cl.getNestedClassifiers().isEmpty());
	}

	public static boolean hasOutgoingAssociations(Class cl) { // 13
		ArrayList<Association> associations = getAllAssociations(cl.getModel());
		for (Association association : associations) {
			for (Property associationEnd : association.getMemberEnds()) {
				if (associationEnd.getType().equals(cl) && (! associationEnd.isNavigable()))
					return true;
			}
		}		
		return false;
	}

	private static ArrayList<Association> getAllAssociations(Model model) { // 7
		ArrayList<Association> associations = new ArrayList<Association>();
		TreeIterator<EObject> iter = model.eResource().getAllContents();
		while (iter.hasNext()) {
			EObject eObject = iter.next();
			if (eObject instanceof Association) {
				associations.add((Association) eObject);
			}			
		}
		return associations;
	}

	public static boolean hasIncomingAssociations(Class cl) { // 13
		ArrayList<Association> associations = getAllAssociations(cl.getModel());
		for (Association association : associations) {
			for (Property associationEnd : association.getMemberEnds()) {
				if (associationEnd.getType().equals(cl) && associationEnd.isNavigable())
					return true;
			}
		}		
		return false;
	}

	public static boolean implementsInterfaces(Class cl) { // 1
		return (! cl.getInterfaceRealizations().isEmpty());
	}

	public static boolean usesInterfaces(Class cl) { // 14
		ArrayList<Usage> usages = getAllUsages(cl.getModel());
		for (Usage usage : usages) {
			if (usage.getClients().contains(cl)) {
				EList<NamedElement> suppliers = usage.getSuppliers();
				for (NamedElement supplier : suppliers) {
					if (supplier instanceof Interface) return true;
				}
			}
		}
		return false;
	}

	private static ArrayList<Usage> getAllUsages(Model model) { // 7
		ArrayList<Usage> usages = new ArrayList<Usage>();
		TreeIterator<EObject> iter = model.eResource().getAllContents();
		while (iter.hasNext()) {
			EObject eObject = iter.next();
			if (eObject instanceof Usage) {
				usages.add((Usage) eObject);
			}			
		}
		return usages;
	}

	public static boolean isUsedAsAttributeType(Class cl) { // 12
		ArrayList<Property> attributes = getAllAttributes(cl.getModel());
		for (Property attribute : attributes) {
			if (attribute.getType() != null 
					&& attribute.getType().equals(cl)) return true;
		}
		return false;
	}

	private static ArrayList<Property> getAllAttributes(Model model) { // 8
		ArrayList<Property> attributes = new ArrayList<Property>();
		TreeIterator<EObject> iter = model.eResource().getAllContents();
		while (iter.hasNext()) {
			EObject eObject = iter.next();
			if (eObject instanceof Property) {
				Property attribute = (Property) eObject;
				if (attribute.getClass_() != null) attributes.add(attribute);
			}			
		}		
		return attributes;
	}

	public static boolean isUsedAsParameterType(Class cl) { // 11
		ArrayList<Parameter> parameters = getAllParameters(cl.getModel());
		for (Parameter parameter : parameters) {
			if (parameter.getType() != null 
					&& parameter.getType().equals(cl)) return true;
		}
		return false;
	}

	private static ArrayList<Parameter> getAllParameters(Model model) { // 7
		ArrayList<Parameter> parameters = new ArrayList<Parameter>();
		TreeIterator<EObject> iter = model.eResource().getAllContents();
		while (iter.hasNext()) {
			EObject eObject = iter.next();
			if (eObject instanceof Parameter) {
				parameters.add((Parameter) eObject);
			}			
		}		
		return parameters;
	}

	public static EList<Type> getTypes(Model model) { // 11
		EList<Type> types = new BasicEList<Type>();
		TreeIterator<EObject> iter = model.eResource().getAllContents();
		while (iter.hasNext()) {
			EObject eObject = iter.next();
			if ((eObject instanceof Type)
					&&  !(eObject instanceof Association)) {
				types.add((Type) eObject);
			}			
		}		
		EList<PackageableElement> importedElements = model.getImportedMembers();
		for (PackageableElement elem : importedElements) {
			if ((elem instanceof Type)
					&&  !(elem instanceof Association)) {
				types.add((Type) elem);
			}
		}
		return types;
	}

	public static EList<NamedElement> getNamedElementsFromList( // 4
							EList<Type> types, String name) {
		EList<NamedElement> namedElements = new BasicEList<NamedElement>();
		for (Type ne : types) {
			if (ne.getName().equals(name)) namedElements.add(ne);
		}		
		return namedElements;
	}

	public static boolean hasSubclasses(Class cl) { // 11
		ArrayList<Class> classes = getAllClasses(cl.getModel());
		for (Class cls : classes) {
			if (cls.getSuperClasses().contains(cl)) return true;
		}
		return false;
	}

	public static ArrayList<Class> getAllClasses(Model model) { // 7
		ArrayList<Class> classes = new ArrayList<Class>();
		TreeIterator<EObject> iter = model.eResource().getAllContents();
		while (iter.hasNext()) {
			EObject eObject = iter.next();
			if (eObject instanceof Class) {
				classes.add((Class) eObject);
			}			
		}		
		return classes;
	}

	private static Collection<? extends Class> getOtherAssociationEndsTypeClasses( // 6
			Property assocend) {
		ArrayList<Class> classes = new ArrayList<Class>();
		Association assoc = assocend.getAssociation();
		for (Property ae : assoc.getMemberEnds()) {
			if ((! ae.equals(assocend)) 
					&& (! ae.getType().equals(assocend.getType()))) {
				classes.add((Class) ae.getType());
			}
		}
		return classes;
	}

	public static List<Class> getOne2OneAssociatedClasses(Class cl) { // 59
		ArrayList<Class> result = new ArrayList<Class>();
		ArrayList<Association> incomingAssociations = 
									getIncomingAssociations(cl);
		ArrayList<Association> outgoingAssociations = 
									getOutgoingAssociations(cl);
		ArrayList<Class> classesFromIncomingAssociations = 
			getOtherClassesFromAssociations(cl, incomingAssociations);
		ArrayList<Class> classesFromOutgoingAssociations = 
				getOtherClassesFromAssociations(cl, outgoingAssociations);
		ArrayList<Class> associatedClasses = new ArrayList<Class>();
		for (Class associatedClass : classesFromIncomingAssociations) {
			if (classesFromOutgoingAssociations.contains(associatedClass))
				associatedClasses.add(associatedClass);
		}
		for (Class associatedClass : associatedClasses) {
			if (incomingAssociationHasOne2OneMultiplicityAndIsPublic
					(cl, incomingAssociations, associatedClass)
					&& outgoingAssociationHasOne2OneMultiplicityAndIsPublic
						(cl, outgoingAssociations, associatedClass)){
				result.add(associatedClass);
			}
		}
		return result;
	}

	private static boolean outgoingAssociationHasOne2OneMultiplicityAndIsPublic( // 8
			Class cl,
			ArrayList<Association> outgoingAssociations, 
			Class associatedClass) {
		for (Association assoc : outgoingAssociations) {
			for (Property ae1 : assoc.getMemberEnds()) {
				if (ae1.getType().equals(cl)) {
					for (Property ae2 : assoc.getMemberEnds()) {
						if (ae2.getType().equals(associatedClass)) {
							if ((ae2.getLower() == 1) 
									&& (ae2.getUpper() == 1)
									&& (ae2.getVisibility().equals(VisibilityKind.PUBLIC_LITERAL)))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static boolean incomingAssociationHasOne2OneMultiplicityAndIsPublic( // 8
			Class cl, 
			ArrayList<Association> incomingAssociations, 
			Class associatedClass) {
		for (Association assoc : incomingAssociations) {
			for (Property ae1 : assoc.getMemberEnds()) {
				if (ae1.getType().equals(cl)) {
					for (Property ae2 : assoc.getMemberEnds()) {
						if (ae2.getType().equals(associatedClass)) {
							if ((ae1.getLower() == 1) 
									&& (ae1.getUpper() == 1)
									&& (ae2.getVisibility().equals(VisibilityKind.PUBLIC_LITERAL)))
								return true;
						}
					}
				}
			}
		}
		return false;
	}

	private static ArrayList<Class> getOtherClassesFromAssociations( // 8
					Class cl, ArrayList<Association> incomingAssociations) { 
		ArrayList<Class> result = new ArrayList<Class>();
		for (Association assoc : incomingAssociations) {
			for (Property associationEnd1 : assoc.getMemberEnds()) {
				if (associationEnd1.getType().equals(cl)){
					for (Property associationEnd2 : assoc.getMemberEnds()) {
						if ((! associationEnd1.equals(associationEnd2)) 
								&& associationEnd2.getType() instanceof Class
								&& (! associationEnd2.getType().equals(cl))) {
							result.add((Class) associationEnd2.getType());
						}
					}
				}
			}
		}
		return result;
	}

	private static ArrayList<Association> getOutgoingAssociations(Class cl) { // 16
		ArrayList<Association> associations = getAllAssociations(cl.getModel());
		ArrayList<Association> result = new ArrayList<Association>();
		for (Association association : associations) {
			for (Property associationEnd1 : association.getMemberEnds()) {
				if (associationEnd1.getType().equals(cl)){
					for (Property associationEnd2 : association.getMemberEnds()) {
						if ((! associationEnd1.equals(associationEnd2)) 
								&& associationEnd2.isNavigable()) {
							result.add(association);
						}
					}
				}
			}
		}		
		return result;
	}

	private static ArrayList<Association> getIncomingAssociations(Class cl) { // 14
		ArrayList<Association> associations = getAllAssociations(cl.getModel());
		ArrayList<Association> result = new ArrayList<Association>();
		for (Association association : associations) {
			for (Property associationEnd : association.getMemberEnds()) {
				if (associationEnd.getType().equals(cl) && associationEnd.isNavigable())
					result.add(association);
			}
		}		
		return result;
	}
	
	public static Class getAssociatedClass(List<Class> classes, String className) { // 4
		Class associatedClass = null;
		for (Class cl : classes) {
			if (cl.getName().equals(className)) associatedClass = cl;
		}
		return associatedClass;
	}

	public static boolean isAssociatedClass(List<Class> classes, String className) { // 3
		for (Class cl : classes) {
			if (cl.getName().equals(className)) return true;
		}
		return false;
	}
	
	private static boolean haveSameStaticProperties(Operation op, Operation oper) { // 1
		return (op.isStatic() == oper.isStatic());
	}

	private static boolean haveSameAbstractProperties(Operation op, // 1
			Operation oper) {
		return (op.isAbstract() == oper.isAbstract());
	}

	private static boolean haveSameQueryProperties(Operation op, Operation oper) { // 1
		return (op.isQuery() == oper.isQuery());
	}

	private static boolean haveSameLeafProperties(Operation op, Operation oper) { // 1
		return (op.isLeaf() == oper.isLeaf());
	}

	private static boolean haveSameMultiplicities(Operation op, Operation oper) { // 1
		return ((op.getLower() == oper.getLower()) 
				&& (op.getUpper() == oper.getUpper()));
	}

	private static boolean haveSameVisibilities(Operation op, Operation oper) { // 1
		return op.getVisibility().equals(oper.getVisibility());
	}

	public static boolean haveSameType(Operation op, Operation oper) { // 3
		if ((op.getType() == null) && (oper.getType() == null)) return true;
		if ((op.getType() != null) 
				&& (oper.getType() != null)
				&& (op.getType().equals(oper.getType()))) return true;
		return false;
	}

	public static boolean haveSameNames(Operation op, Operation oper) { // 1
		return op.getName().equals(oper.getName());
	}

	private static int getOtherSideUpper(Property attribute) { // 4
		int upper = 0;
		for (Property ae : attribute.getAssociation().getMemberEnds()) {
			if (! ae.equals(attribute)) upper = ae.getUpper();
		}
		return upper;
	}

	private static boolean haveSameDerivedUnionProperties(Property att1, Property att2) { // 1
		return (att1.isDerivedUnion() == att2.isDerivedUnion());
	}
	
	private static boolean haveSameStaticProperties(Property att1, Property att2) { // 1
		return (att1.isStatic() == att2.isStatic());
	}

	private static boolean haveSameOrderedProperties(Property att1, Property att2) { // 1
		return (att1.isOrdered() == att2.isOrdered());
	}
	
	private static boolean haveSameUniqueProperties(Property att1, Property att2) { // 1
		return (att1.isUnique() == att2.isUnique());
	}
	
	private static boolean haveSameReadOnlyProperties(Property att1, Property att2) { // 1
		return (att1.isReadOnly() == att2.isReadOnly());
	}
	
	private static boolean haveSameLeafProperties(Property att1, Property att2) { // 1
		return (att1.isLeaf() == att2.isLeaf());
	}
	
	private static boolean haveSameDerivedProperties(Property att1, Property att2) { // 1
		return (att1.isDerived() == att2.isDerived());
	}

	private static boolean haveSameDefaultValues(Property att1, Property att2) { // 3
		if ((att1.getDefault() == null) && (att2.getDefault() == null)) return true;
		if ((att1.getDefault() != null) 
				&& (att2.getDefault() != null)
				&& (att1.getDefault().equals(att2.getDefault()))) return true;
		return false;
	}

	private static boolean haveSameAggregations(Property att1, Property att2) { // 1
		return att1.getAggregation().equals(att2.getAggregation());
	}

	private static boolean haveSameMultiplicities(Property att1, Property att2) { // 1
		return ((att1.getLower() == att2.getLower()) 
				&& (att1.getUpper() == att2.getUpper()));
	}

	private static boolean haveSameVisibilities(Property att1, Property att2) { // 1
		return att1.getVisibility().equals(att2.getVisibility());
	}

	private static boolean haveSameNames(Property att1, Property att2) { // 1
		return att1.getName().equals(att2.getName());
	}

	private static boolean haveSameType(Property att1, Property att2) { // 3
		if ((att1.getType() == null) && (att2.getType() == null)) return true;
		if ((att1.getType() != null) 
				&& (att2.getType() != null)
				&& (att1.getType().equals(att2.getType()))) return true;
		return false;
	}

	public static ArrayList<Class> getAllSubClasses(Class superClass) { // 12
		ArrayList<Class> classes = getAllClasses(superClass.getModel());
		ArrayList<Class> subClasses = new ArrayList<Class>();
		for (Class cls : classes) {
			if (cls.getSuperClasses().contains(superClass)) subClasses.add(cls);
		}
		return subClasses;
	}

	public static boolean operationHasParameters(Operation op, // 16
			ArrayList<Parameter> parameters) {
		for (Parameter param : parameters) {
			if (! operationHasParameter(op, param)) return false;
		}
		return true;
	}

	private static boolean operationHasParameter(Operation op, Parameter param) { // 13
		for (Parameter par : op.getOwnedParameters()) {
			if (haveSameNames(param, par) 
					&& haveSameTypes(param, par) 
					&& haveSameDirections(param, par) 
					&& haveSameMultiplicities(param, par)) {
				return true;
			}
		}
		return false;
	}

	public static Parameter getEqualParameterInOperation(Operation op, // 13
			Parameter param) {
		for (Parameter par : op.getOwnedParameters()) {
			if (haveSameNames(param, par) 
					&& haveSameTypes(param, par) 
					&& haveSameDirections(param, par) 
					&& haveSameMultiplicities(param, par)) {
				return par;
			}
		}
		return null;
	}

	public static boolean isInputParameter(Parameter param) { // 3
		if (param.getDirection().equals(ParameterDirectionKind.IN_LITERAL)) return true;
		if (param.getDirection().equals(ParameterDirectionKind.INOUT_LITERAL)) return true;
		return false;
	}
	
	public static boolean oneSubClassInheritsEquallyOperation( // 47
			Class superClass,
			Operation operation) {
		ArrayList<Class> classes = getAllSubClasses(superClass);
		for (Class cls : classes) {
			for (NamedElement ne: cls.getInheritedMembers()) {
				if ((! ne.equals(operation))
						&& (ne instanceof Operation)) {
					Operation op = (Operation) ne;
					if (haveSameNames(op, operation)
							&& haveSameType(op, operation)
							&& haveSameProperties(op.getOwnedParameters(), operation.getOwnedParameters())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean oneSubClassHasEquallyOperation( // 47
			Class superClass,
			Operation operation) {
		ArrayList<Class> classes = getAllSubClasses(superClass);
		for (Class cls : classes) {
			for (Operation op : cls.getOwnedOperations()) {
				if (haveSameNames(op, operation)
						&& haveSameType(op, operation)
						&& haveSameProperties(op.getOwnedParameters(), operation.getOwnedParameters())) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean oneSubClassInheritsEquallyNamedAttribute( // 18
			Class superClass, 
			Property attribute) {
		ArrayList<Class> classes = getAllSubClasses(superClass);
		for (Class cls : classes) {
			for (NamedElement ne: cls.getInheritedMembers()) {
				if ((! ne.equals(attribute)) 
						&& (ne.getName().equals(attribute.getName()))) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean oneSubClassHasEquallyNamedAttribute( // 18
			Class superClass, 
			Property attribute) {
		ArrayList<Class> classes = getAllSubClasses(superClass);
		for (Class cls : classes) {
			for (Property attr : cls.getOwnedAttributes()) {
				if (attr.getName().equals(attribute.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	private static ArrayList<String> getReasonsWhySubClassDoesNotHaveOperation( // 55
			Class cls, Operation op) {
		ArrayList<String> reasons = new ArrayList<String>();
		Operation equalOperation = null;
		for (Operation oper : cls.getOwnedOperations()) {
			if (haveSameNames(op, oper)
					&& haveSameType(op, oper)
					&& haveSameProperties(op.getOwnedParameters(), oper.getOwnedParameters())) {
				equalOperation = oper;
				break;
			}
		}
		if (equalOperation == null) {
			reasons.add("Class '" + cls.getName() + 
					"' does not have an operation named '" + op.getName() + 
					"' and with the same type and parameter list!");
		} else {
			if (! haveSameVisibilities(equalOperation, op)) 
				reasons.add("Class '" + cls.getName() + "' has an operation named '" + 
						op.getName() + "' of different visibility!");
			if (! haveSameMultiplicities(equalOperation, op)) 
				reasons.add("Class '" + cls.getName() + "' has an operation named '" + 
						op.getName() + "' with different multiplicity!");
			if (! haveSameLeafProperties(equalOperation, op)) 
				reasons.add("Class '" + cls.getName() + "' has an operation named '" + 
						op.getName() + "' with different leaf property!");
			if (! haveSameStaticProperties(equalOperation, op))
				reasons.add("Class '" + cls.getName() + "' has an operation named '" + 
						op.getName() + "' with different static property!");
			if (! haveSameQueryProperties(equalOperation, op))
				reasons.add("Class '" + cls.getName() + "' has an operation named '" + 
						op.getName() + "' with different query property!");
			if (! haveSameAbstractProperties(equalOperation, op))
				reasons.add("Class '" + cls.getName() + "' has an operation named '" + 
						op.getName() + "' with different abstract property!");
		}
		return reasons;
	}

	
	public static ArrayList<String> getReasonsWhySubClassesDoNotHaveOperation( // 74
			Class superClass, Operation op) {
		ArrayList<Class> classes = getAllSubClasses(superClass);
		ArrayList<String> reasons = new ArrayList<String>();
		for (Class cls : classes) {
			ArrayList<String> reasonsWhySubClassDoesNotHaveOperation =
					getReasonsWhySubClassDoesNotHaveOperation(cls, op);
			if (! reasonsWhySubClassDoesNotHaveOperation.isEmpty()) {
				reasons.addAll(reasonsWhySubClassDoesNotHaveOperation);
			}
		}
		return reasons;
	}


	private static ArrayList<String> getReasonsWhyClassDoesNotHaveAttribute( // 58
			Class cls, Property attribute) {
		ArrayList<String> reasons = new ArrayList<String>();
		Property equalAttribute = null;
		for (Property attr : cls.getOwnedAttributes()) {
			if (haveSameNames(attr, attribute)) {
				equalAttribute = attr;
				break;
			}
		}
		if (equalAttribute == null) {
			reasons.add("Class '" + cls.getName() + 
					"' does not have an attribute named '" + 
					attribute.getName() + "'!");
		} else {
			if (! haveSameType(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' of different type!");
			if (! haveSameVisibilities(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' of different visibility!");
			if (! haveSameMultiplicities(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
					attribute.getName() + "' with different multiplicity!");
			if (! haveSameAggregations(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' of different aggregation kind!");
			if (! haveSameDefaultValues(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different default value!");
			if (! haveSameDerivedProperties(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different derived property!");
			if (! haveSameLeafProperties(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different leaf property!");
			if (! haveSameReadOnlyProperties(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different read only property!");
			if (! haveSameUniqueProperties(equalAttribute, attribute)) 
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different unique property!");
			if (! haveSameOrderedProperties(equalAttribute, attribute))
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different ordered property!");
			if (! haveSameStaticProperties(equalAttribute, attribute))
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different static property!");
			if (! haveSameDerivedUnionProperties(equalAttribute, attribute))
				reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
						attribute.getName() + "' with different derived union property!");
			if ((equalAttribute.getAssociation() != null) && (attribute.getAssociation() != null)) {
				if (getOtherSideUpper(equalAttribute) != getOtherSideUpper(attribute))
					reasons.add("Class '" + cls.getName() + "' has an attribute named '" + 
							attribute.getName() + "' as end of an association whose " +
									"opposite end has an upper multiplicity bound " +
									"different from " + getOtherSideUpper(attribute) + "!");
			}
		}
		return reasons;
	}

	public static ArrayList<String> getReasonsWhySubClassesDoNotHaveAttribute( // 77
			Class superClass, Property attribute) {
		ArrayList<Class> classes = getAllSubClasses(superClass);
		ArrayList<String> reasons = new ArrayList<String>();
		for (Class cls : classes) {
			ArrayList<String> reasonsWhySubClassDoesNotHaveAttribute =
					getReasonsWhyClassDoesNotHaveAttribute(cls, attribute);
			if (! reasonsWhySubClassDoesNotHaveAttribute.isEmpty()) {
				reasons.addAll(reasonsWhySubClassDoesNotHaveAttribute);
			}
		}
		return reasons;
	}
	
	public static boolean classHasEqualOperation(Class cls, Operation op) { // 48
		if (op.getClass_().equals(cls)) return true;
		boolean classHasEqualOperation = false;
		for (Operation oper : cls.getOwnedOperations()) {
			if (! haveSameNames(op, oper)) continue;
			if (! haveSameType(op, oper)) continue;
			if (! haveSameVisibilities(op, oper)) continue;
			if (! haveSameMultiplicities(op, oper)) continue;
			if (! haveSameProperties(op.getOwnedParameters(), oper.getOwnedParameters())) continue;
			if (! haveSameLeafProperties(op, oper)) continue;
			if (! haveSameQueryProperties(op, oper)) continue;
			if (! haveSameAbstractProperties(op, oper)) continue;
			if (! haveSameStaticProperties(op, oper)) continue;
			classHasEqualOperation = true;
			break;
		}		
		return classHasEqualOperation;
	}

	private static boolean classHasEqualAttribute(Class cls, Property attribute) { // 41
		if (attribute.getClass_().equals(cls)) return true;
		boolean classHasEqualAttribute = false;
		for (Property attr : cls.getOwnedAttributes()) {
			if (! haveSameNames(attr, attribute)) continue;
			if (! haveSameType(attr, attribute)) continue;
			if (! haveSameVisibilities(attr, attribute)) continue;
			if (! haveSameMultiplicities(attr, attribute)) continue;
			if (! haveSameAggregations(attr, attribute)) continue;
			if (! haveSameDefaultValues(attr, attribute)) continue;
			if (! haveSameDerivedProperties(attr, attribute)) continue;
			if (! haveSameLeafProperties(attr, attribute)) continue;
			if (! haveSameReadOnlyProperties(attr, attribute)) continue;
			if (! haveSameUniqueProperties(attr, attribute)) continue;
			if (! haveSameOrderedProperties(attr, attribute)) continue;
			if (! haveSameStaticProperties(attr, attribute)) continue;
			if (! haveSameDerivedUnionProperties(attr, attribute)) continue;
			if ((attr.getAssociation() != null) && (attribute.getAssociation() != null)) {
				if (getOtherSideUpper(attr) != getOtherSideUpper(attribute)) continue;
			}
			classHasEqualAttribute = true;
			break;
		}
		return classHasEqualAttribute;
	}

	public static boolean subClassesHaveAttribute(Class superClass, Property attribute) { // 60
		ArrayList<Class> classes = getAllSubClasses(superClass);
		boolean eachSubclassHasAttribute = true;
		for (Class cls : classes) {
			if (! classHasEqualAttribute(cls, attribute)) {
				eachSubclassHasAttribute = false;
				break;
			}
		}
		return eachSubclassHasAttribute;
	}
	
	public static boolean subClassesHaveOperation(Class superClass, Operation operation) { // 67
		ArrayList<Class> classes = getAllSubClasses(superClass);
		boolean eachSubclassHasAttribute = true;
		for (Class cls : classes) {
			if (! classHasEqualOperation(cls, operation)) {
				eachSubclassHasAttribute = false;
				break;
			}
		}
		return eachSubclassHasAttribute;
	}

	public static List<Association> getAssociations(Class cl) { // 14
		ArrayList<Association> allAssociations = getAllAssociations(cl.getModel());
		ArrayList<Association> associations = new ArrayList<Association>();
		for (Association association : allAssociations) {
			for (Property associationEnd : association.getMemberEnds()) {
				if (associationEnd.getType().equals(cl))
					if (! associations.contains(association)) associations.add(association);
			}
		}		
		return associations;
	}

	public static boolean isUsedAsFurtherAttributeType(Class cl) { // 12
		ArrayList<Property> attributes = getAllAttributes(cl.getModel());
		for (Property attribute : attributes) {
			if (attribute.getType() != null 
					&& attribute.getType().equals(cl)
					&& (attribute.getAssociation() == null)) return true;
		}
		return false;
	}

	public static ArrayList<Class> getOtherAssociatedClasses(Class cl) { // 20
		ArrayList<Class> classes = new ArrayList<Class>();
		ArrayList<Association> associations = getAllAssociations(cl.getModel());
		for (Association assoc : associations) {
			for (Property assocend : assoc.getMemberEnds()) {
				if (assocend.getType().equals(cl)) {
					classes.addAll(getOtherAssociationEndsTypeClasses(assocend));
				}
			}
		}
		return classes;
	}

}
