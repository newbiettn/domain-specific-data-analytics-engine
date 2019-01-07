import JSHOP2.*;

class Operator0 extends Operator
{
	public Operator0()
	{
		super(new Predicate(0, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(0.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(0, 1));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionNil(1)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator1 extends Operator
{
	public Operator1()
	{
		super(new Predicate(1, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(0.0));


		DelAddElement[] delIn = new DelAddElement[1];
		delIn[0] = new DelAddAtomic(new Predicate(0, 1));

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[0];

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionNil(1)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition1 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition1(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(6, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(7), new TermList(new TermList(TermVariable.getVariable(1), TermList.NIL), TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator2 extends Operator
{
	public Operator2()
	{
		super(new Predicate(2, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(8, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition1(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition2 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition2(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(6, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(9), new TermList(new TermList(TermVariable.getVariable(1), TermList.NIL), TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator3 extends Operator
{
	public Operator3()
	{
		super(new Predicate(3, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(8, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition2(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator4 extends Operator
{
	public Operator4()
	{
		super(new Predicate(4, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(11, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(10), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition3 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition3(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(12, 3, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(13), new TermList(new TermList(TermVariable.getVariable(2), TermList.NIL), TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator5 extends Operator
{
	public Operator5()
	{
		super(new Predicate(5, 3, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(14, 3, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition3(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition4 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition4(Term[] unifier)
	{
		p = new Precondition[5];
		p[1] = new PreconditionAtomic(new Predicate(15, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(4, 4, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(12, 4, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[4] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(16), new TermList(new TermList(TermVariable.getVariable(3), TermList.NIL), TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[5][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[4] == null)
		{
			boolean b3changed = false;
			while (b[3] == null)
			{
				boolean b2changed = false;
				while (b[2] == null)
				{
					boolean b1changed = false;
					while (b[1] == null)
					{
						b[1] = p[1].nextBinding();
						if (b[1] == null)
							return null;
						b1changed = true;
					}
					if ( b1changed ) {
						p[2].reset();
						p[2].bind(Term.merge(b, 2));
					}
					b[2] = p[2].nextBinding();
					if (b[2] == null) b[1] = null;
					b2changed = true;
				}
				if ( b2changed ) {
					p[3].reset();
					p[3].bind(Term.merge(b, 3));
				}
				b[3] = p[3].nextBinding();
				if (b[3] == null) b[2] = null;
				b3changed = true;
			}
			if ( b3changed ) {
				p[4].reset();
				p[4].bind(Term.merge(b, 4));
			}
			b[4] = p[4].nextBinding();
			if (b[4] == null) b[3] = null;
		}

		Term[] retVal = Term.merge(b, 5);
		b[4] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}
}

class Operator6 extends Operator
{
	public Operator6()
	{
		super(new Predicate(6, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[2];
		addIn[0] = new DelAddAtomic(new Predicate(17, 4, new TermList(TermVariable.getVariable(1), new TermList(new TermNumber(0.0), TermList.NIL))));
		addIn[1] = new DelAddAtomic(new Predicate(18, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition4(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition5 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition5(Term[] unifier)
	{
		p = new Precondition[5];
		p[1] = new PreconditionAtomic(new Predicate(15, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(4, 4, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(19, 4, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[4] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(20), new TermList(new TermList(TermVariable.getVariable(3), TermList.NIL), TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[5][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[4] == null)
		{
			boolean b3changed = false;
			while (b[3] == null)
			{
				boolean b2changed = false;
				while (b[2] == null)
				{
					boolean b1changed = false;
					while (b[1] == null)
					{
						b[1] = p[1].nextBinding();
						if (b[1] == null)
							return null;
						b1changed = true;
					}
					if ( b1changed ) {
						p[2].reset();
						p[2].bind(Term.merge(b, 2));
					}
					b[2] = p[2].nextBinding();
					if (b[2] == null) b[1] = null;
					b2changed = true;
				}
				if ( b2changed ) {
					p[3].reset();
					p[3].bind(Term.merge(b, 3));
				}
				b[3] = p[3].nextBinding();
				if (b[3] == null) b[2] = null;
				b3changed = true;
			}
			if ( b3changed ) {
				p[4].reset();
				p[4].bind(Term.merge(b, 4));
			}
			b[4] = p[4].nextBinding();
			if (b[4] == null) b[3] = null;
		}

		Term[] retVal = Term.merge(b, 5);
		b[4] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}
}

class Operator7 extends Operator
{
	public Operator7()
	{
		super(new Predicate(7, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[0];

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition5(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator8 extends Operator
{
	public Operator8()
	{
		super(new Predicate(8, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(21), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator9 extends Operator
{
	public Operator9()
	{
		super(new Predicate(9, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(23), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator10 extends Operator
{
	public Operator10()
	{
		super(new Predicate(10, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(24), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator11 extends Operator
{
	public Operator11()
	{
		super(new Predicate(11, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(25), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator12 extends Operator
{
	public Operator12()
	{
		super(new Predicate(12, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(26), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator13 extends Operator
{
	public Operator13()
	{
		super(new Predicate(13, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(27), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator14 extends Operator
{
	public Operator14()
	{
		super(new Predicate(14, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(28), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator15 extends Operator
{
	public Operator15()
	{
		super(new Predicate(15, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(30, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(29), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator16 extends Operator
{
	public Operator16()
	{
		super(new Predicate(16, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[1];
		addIn[0] = new DelAddAtomic(new Predicate(32, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(31), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Operator17 extends Operator
{
	public Operator17()
	{
		super(new Predicate(17, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[2];
		addIn[0] = new DelAddAtomic(new Predicate(34, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));
		addIn[1] = new DelAddAtomic(new Predicate(36, 1, new TermList(TermVariable.getVariable(0), new TermList(TermConstant.getConstant(35), TermList.NIL))));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new PreconditionCall(new TermCall(new List(TermConstant.getConstant(33), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition6 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition6(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(38), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator18 extends Operator
{
	public Operator18()
	{
		super(new Predicate(18, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[1];
		delIn[0] = new DelAddAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)));

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[2];
		addIn[0] = new DelAddAtomic(new Predicate(39, 2, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(0), TermList.NIL))));
		addIn[1] = new DelAddAtomic(new Predicate(41, 2, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(40), TermList.NIL))));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition6(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition7 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition7(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(42), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator19 extends Operator
{
	public Operator19()
	{
		super(new Predicate(19, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[1];
		delIn[0] = new DelAddAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)));

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[2];
		addIn[0] = new DelAddAtomic(new Predicate(39, 2, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(0), TermList.NIL))));
		addIn[1] = new DelAddAtomic(new Predicate(41, 2, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(43), TermList.NIL))));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition7(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition8 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition8(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(44), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator20 extends Operator
{
	public Operator20()
	{
		super(new Predicate(20, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[1];
		delIn[0] = new DelAddAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)));

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[2];
		addIn[0] = new DelAddAtomic(new Predicate(39, 2, new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(0), TermList.NIL))));
		addIn[1] = new DelAddAtomic(new Predicate(41, 2, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(45), TermList.NIL))));

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition8(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition9 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition9(Term[] unifier)
	{
		p = new Precondition[5];
		p[1] = new PreconditionAtomic(new Predicate(34, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionNegation(new PreconditionAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 2);
		p[3] = new PreconditionAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[4] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(46), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[5][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[4] == null)
		{
			boolean b3changed = false;
			while (b[3] == null)
			{
				boolean b2changed = false;
				while (b[2] == null)
				{
					boolean b1changed = false;
					while (b[1] == null)
					{
						b[1] = p[1].nextBinding();
						if (b[1] == null)
							return null;
						b1changed = true;
					}
					if ( b1changed ) {
						p[2].reset();
						p[2].bind(Term.merge(b, 2));
					}
					b[2] = p[2].nextBinding();
					if (b[2] == null) b[1] = null;
					b2changed = true;
				}
				if ( b2changed ) {
					p[3].reset();
					p[3].bind(Term.merge(b, 3));
				}
				b[3] = p[3].nextBinding();
				if (b[3] == null) b[2] = null;
				b3changed = true;
			}
			if ( b3changed ) {
				p[4].reset();
				p[4].bind(Term.merge(b, 4));
			}
			b[4] = p[4].nextBinding();
			if (b[4] == null) b[3] = null;
		}

		Term[] retVal = Term.merge(b, 5);
		b[4] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}
}

class Operator21 extends Operator
{
	public Operator21()
	{
		super(new Predicate(21, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[1];
		delIn[0] = new DelAddAtomic(new Predicate(37, 2, new TermList(TermVariable.getVariable(1), TermList.NIL)));

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[0];

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition9(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition10 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition10(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionNegation(new PreconditionAtomic(new Predicate(37, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 1);
		p[2] = new PreconditionCall(new TermCall(new List(TermConstant.getConstant(47), new TermList(TermList.NIL, TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Operator22 extends Operator
{
	public Operator22()
	{
		super(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), -1, -1, new TermNumber(1.0));


		DelAddElement[] delIn = new DelAddElement[0];

		setDel(delIn);

		DelAddElement[] addIn = new DelAddElement[0];

		setAdd(addIn);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		p = (new Precondition10(unifier)).setComparator(null);
		p.reset();

		return p;
	}
}

class Precondition11 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition11(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(49, 2, new TermList(TermVariable.getVariable(0), new TermList(TermConstant.getConstant(48), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Precondition12 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition12(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(49, 2, new TermList(TermVariable.getVariable(0), new TermList(TermConstant.getConstant(51), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Method0 extends Method
{
	public Method0()
	{
		super(new Predicate(0, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)));
		TaskList[] subsIn = new TaskList[2];

		subsIn[0] = createTaskList0();
		subsIn[1] = createTaskList1();

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(2, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(4, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(5, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList1()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(3, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(4, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(5, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), false, true));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new Precondition11(unifier)).setComparator(null);
			break;
			case 1:
				p = (new Precondition12(unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method0Branch0";
			case 1: return "Method0Branch1";
			default: return null;
		}
	}
}

class Precondition13 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition13(Term[] unifier)
	{
		p = new Precondition[7];
		p[1] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(1), TermList.NIL), datamining.calculateCheckListEmpty, "datamining.calculateCheckListEmpty"), new TermList(new TermNumber(0.0), TermList.NIL)), StdLib.equal, "StdLib.equal"), unifier);
		p[2] = new PreconditionAtomic(new Predicate(0, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(1, 5, new TermList(TermVariable.getVariable(3), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[5] = new PreconditionCall(new TermCall(new List(TermVariable.getVariable(4), new TermList(new TermNumber(30.0), TermList.NIL)), StdLib.lessEq, "StdLib.lessEq"), unifier);
		p[6] = new PreconditionCall(new TermCall(new List(TermVariable.getVariable(4), new TermList(new TermNumber(0.0), TermList.NIL)), StdLib.more, "StdLib.more"), unifier);
		b = new Term[7][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
		b[6] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[6] == null)
		{
			boolean b5changed = false;
			while (b[5] == null)
			{
				boolean b4changed = false;
				while (b[4] == null)
				{
					boolean b3changed = false;
					while (b[3] == null)
					{
						boolean b2changed = false;
						while (b[2] == null)
						{
							boolean b1changed = false;
							while (b[1] == null)
							{
								b[1] = p[1].nextBinding();
								if (b[1] == null)
									return null;
								b1changed = true;
							}
							if ( b1changed ) {
								p[2].reset();
								p[2].bind(Term.merge(b, 2));
							}
							b[2] = p[2].nextBinding();
							if (b[2] == null) b[1] = null;
							b2changed = true;
						}
						if ( b2changed ) {
							p[3].reset();
							p[3].bind(Term.merge(b, 3));
						}
						b[3] = p[3].nextBinding();
						if (b[3] == null) b[2] = null;
						b3changed = true;
					}
					if ( b3changed ) {
						p[4].reset();
						p[4].bind(Term.merge(b, 4));
					}
					b[4] = p[4].nextBinding();
					if (b[4] == null) b[3] = null;
					b4changed = true;
				}
				if ( b4changed ) {
					p[5].reset();
					p[5].bind(Term.merge(b, 5));
				}
				b[5] = p[5].nextBinding();
				if (b[5] == null) b[4] = null;
				b5changed = true;
			}
			if ( b5changed ) {
				p[6].reset();
				p[6].bind(Term.merge(b, 6));
			}
			b[6] = p[6].nextBinding();
			if (b[6] == null) b[5] = null;
		}

		Term[] retVal = Term.merge(b, 7);
		b[6] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		p[6].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
		b[6] = null;
	}
}

class Precondition14 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition14(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(1), TermList.NIL), datamining.calculateCheckListEmpty, "datamining.calculateCheckListEmpty"), new TermList(new TermNumber(0.0), TermList.NIL)), StdLib.equal, "StdLib.equal"), unifier);
		p[2] = new PreconditionAtomic(new Predicate(0, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(1, 5, new TermList(TermVariable.getVariable(3), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[5] = new PreconditionCall(new TermCall(new List(TermVariable.getVariable(4), new TermList(new TermNumber(30.0), TermList.NIL)), StdLib.more, "StdLib.more"), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition15 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition15(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(1), TermList.NIL), datamining.calculateCheckListEmpty, "datamining.calculateCheckListEmpty"), new TermList(new TermNumber(0.0), TermList.NIL)), StdLib.equal, "StdLib.equal"), unifier);
		p[2] = new PreconditionAtomic(new Predicate(0, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(1, 5, new TermList(TermVariable.getVariable(3), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[5] = new PreconditionCall(new TermCall(new List(TermVariable.getVariable(4), new TermList(new TermNumber(0.0), TermList.NIL)), StdLib.equal, "StdLib.equal"), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Method1 extends Method
{
	public Method1()
	{
		super(new Predicate(1, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))));
		TaskList[] subsIn = new TaskList[4];

		subsIn[0] = createTaskList0();
		subsIn[1] = createTaskList1();
		subsIn[2] = createTaskList2();
		subsIn[3] = TaskList.empty;

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(2, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(6, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(1, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, false));

		return retVal;
	}

	TaskList createTaskList1()
	{
		TaskList retVal;

		retVal = new TaskList(2, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(7, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(1, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, false));

		return retVal;
	}

	TaskList createTaskList2()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(1, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, false));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new Precondition13(unifier)).setComparator(null);
			break;
			case 1:
				p = (new Precondition14(unifier)).setComparator(null);
			break;
			case 2:
				p = (new Precondition15(unifier)).setComparator(null);
			break;
			case 3:
				p = (new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(1), TermList.NIL), datamining.calculateCheckListEmpty, "datamining.calculateCheckListEmpty"), new TermList(new TermNumber(1.0), TermList.NIL)), StdLib.equal, "StdLib.equal"), unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method1Branch0";
			case 1: return "Method1Branch1";
			case 2: return "Method1Branch2";
			case 3: return "Method1Branch3";
			default: return null;
		}
	}
}

class Precondition16 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition16(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(15, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(52, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Method2 extends Method
{
	public Method2()
	{
		super(new Predicate(1, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)));
		TaskList[] subsIn = new TaskList[2];

		subsIn[0] = createTaskList0();
		subsIn[1] = TaskList.empty;

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(2, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(1, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), false, false));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(1, 2, new TermList(new TermList(TermConstant.getConstant(52), new TermList(TermVariable.getVariable(0), TermList.NIL)), TermList.NIL)), false, true));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new Precondition16(unifier)).setComparator(null);
			break;
			case 1:
				p = (new PreconditionNil(2)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method2Branch0";
			case 1: return "Method2Branch1";
			default: return null;
		}
	}
}

class Precondition17 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition17(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(56), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition18 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition18(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(58), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition19 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition19(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(59), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition20 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition20(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(60), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition21 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition21(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(61), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition22 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition22(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(62), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition23 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition23(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.moreEq, "StdLib.moreEq"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		p[5] = new PreconditionAtomic(new Predicate(57, 4, new TermList(TermVariable.getVariable(1), new TermList(TermConstant.getConstant(63), TermList.NIL))), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition24 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition24(Term[] unifier)
	{
		p = new Precondition[5];
		p[1] = new PreconditionAtomic(new Predicate(53, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(54, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(3), TermList.NIL))), unifier);
		p[3] = new PreconditionCall(new TermCall(new List(new TermCall(new List(TermVariable.getVariable(2), new TermList(new TermNumber(50.0), TermList.NIL)), StdLib.mult, "StdLib.mult"), new TermList(TermVariable.getVariable(3), TermList.NIL)), StdLib.less, "StdLib.less"), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(55, 4, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier), 4);
		b = new Term[5][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[4] == null)
		{
			boolean b3changed = false;
			while (b[3] == null)
			{
				boolean b2changed = false;
				while (b[2] == null)
				{
					boolean b1changed = false;
					while (b[1] == null)
					{
						b[1] = p[1].nextBinding();
						if (b[1] == null)
							return null;
						b1changed = true;
					}
					if ( b1changed ) {
						p[2].reset();
						p[2].bind(Term.merge(b, 2));
					}
					b[2] = p[2].nextBinding();
					if (b[2] == null) b[1] = null;
					b2changed = true;
				}
				if ( b2changed ) {
					p[3].reset();
					p[3].bind(Term.merge(b, 3));
				}
				b[3] = p[3].nextBinding();
				if (b[3] == null) b[2] = null;
				b3changed = true;
			}
			if ( b3changed ) {
				p[4].reset();
				p[4].bind(Term.merge(b, 4));
			}
			b[4] = p[4].nextBinding();
			if (b[4] == null) b[3] = null;
		}

		Term[] retVal = Term.merge(b, 5);
		b[4] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
	}
}

class Method3 extends Method
{
	public Method3()
	{
		super(new Predicate(2, 4, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))));
		TaskList[] subsIn = new TaskList[8];

		subsIn[0] = createTaskList0();
		subsIn[1] = createTaskList1();
		subsIn[2] = createTaskList2();
		subsIn[3] = createTaskList3();
		subsIn[4] = createTaskList4();
		subsIn[5] = createTaskList5();
		subsIn[6] = createTaskList6();
		subsIn[7] = TaskList.empty;

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(8, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	TaskList createTaskList1()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(9, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	TaskList createTaskList2()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(10, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	TaskList createTaskList3()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(11, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	TaskList createTaskList4()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(12, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	TaskList createTaskList5()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(13, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	TaskList createTaskList6()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(14, 4, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new Precondition17(unifier)).setComparator(null);
			break;
			case 1:
				p = (new Precondition18(unifier)).setComparator(null);
			break;
			case 2:
				p = (new Precondition19(unifier)).setComparator(null);
			break;
			case 3:
				p = (new Precondition20(unifier)).setComparator(null);
			break;
			case 4:
				p = (new Precondition21(unifier)).setComparator(null);
			break;
			case 5:
				p = (new Precondition22(unifier)).setComparator(null);
			break;
			case 6:
				p = (new Precondition23(unifier)).setComparator(null);
			break;
			case 7:
				p = (new Precondition24(unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method3Branch0";
			case 1: return "Method3Branch1";
			case 2: return "Method3Branch2";
			case 3: return "Method3Branch3";
			case 4: return "Method3Branch4";
			case 5: return "Method3Branch5";
			case 6: return "Method3Branch6";
			case 7: return "Method3Branch7";
			default: return null;
		}
	}
}

class Method4 extends Method
{
	public Method4()
	{
		super(new Predicate(3, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));
		TaskList[] subsIn = new TaskList[1];

		subsIn[0] = createTaskList0();

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(15, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAtomic(new Predicate(65, 1, new TermList(TermVariable.getVariable(0), new TermList(TermConstant.getConstant(64), TermList.NIL))), unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method4Branch0";
			default: return null;
		}
	}
}

class Method5 extends Method
{
	public Method5()
	{
		super(new Predicate(4, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))));
		TaskList[] subsIn = new TaskList[1];

		subsIn[0] = createTaskList0();

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(1, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, false));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(2, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), false, false));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(3, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, false));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAtomic(new Predicate(8, 2, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method5Branch0";
			default: return null;
		}
	}
}

class Precondition25 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition25(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		p[5] = new PreconditionAtomic(new Predicate(67, 5, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition26 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition26(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		p[5] = new PreconditionAtomic(new Predicate(68, 5, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition27 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition27(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		p[5] = new PreconditionAtomic(new Predicate(69, 5, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition28 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition28(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(70, 5, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[5] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition29 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition29(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(71, 5, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[5] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition30 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition30(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(55, 5, new TermList(TermVariable.getVariable(1), TermList.NIL)), unifier);
		p[5] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Precondition31 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition31(Term[] unifier)
	{
		p = new Precondition[6];
		p[1] = new PreconditionAtomic(new Predicate(8, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier);
		p[2] = new PreconditionAtomic(new Predicate(50, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[3] = new PreconditionAtomic(new Predicate(14, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(4), TermList.NIL))), unifier);
		p[4] = new PreconditionAtomic(new Predicate(65, 5, new TermList(TermVariable.getVariable(0), new TermList(TermConstant.getConstant(64), TermList.NIL))), unifier);
		p[5] = new PreconditionNegation(new PreconditionAtomic(new Predicate(66, 5, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 5);
		b = new Term[6][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[5] == null)
		{
			boolean b4changed = false;
			while (b[4] == null)
			{
				boolean b3changed = false;
				while (b[3] == null)
				{
					boolean b2changed = false;
					while (b[2] == null)
					{
						boolean b1changed = false;
						while (b[1] == null)
						{
							b[1] = p[1].nextBinding();
							if (b[1] == null)
								return null;
							b1changed = true;
						}
						if ( b1changed ) {
							p[2].reset();
							p[2].bind(Term.merge(b, 2));
						}
						b[2] = p[2].nextBinding();
						if (b[2] == null) b[1] = null;
						b2changed = true;
					}
					if ( b2changed ) {
						p[3].reset();
						p[3].bind(Term.merge(b, 3));
					}
					b[3] = p[3].nextBinding();
					if (b[3] == null) b[2] = null;
					b3changed = true;
				}
				if ( b3changed ) {
					p[4].reset();
					p[4].bind(Term.merge(b, 4));
				}
				b[4] = p[4].nextBinding();
				if (b[4] == null) b[3] = null;
				b4changed = true;
			}
			if ( b4changed ) {
				p[5].reset();
				p[5].bind(Term.merge(b, 5));
			}
			b[5] = p[5].nextBinding();
			if (b[5] == null) b[4] = null;
		}

		Term[] retVal = Term.merge(b, 6);
		b[5] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		p[3].reset();
		p[4].reset();
		p[5].reset();
		b[1] = null;
		b[2] = null;
		b[3] = null;
		b[4] = null;
		b[5] = null;
	}
}

class Method6 extends Method
{
	public Method6()
	{
		super(new Predicate(5, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))))));
		TaskList[] subsIn = new TaskList[7];

		subsIn[0] = createTaskList0();
		subsIn[1] = createTaskList1();
		subsIn[2] = createTaskList2();
		subsIn[3] = createTaskList3();
		subsIn[4] = createTaskList4();
		subsIn[5] = createTaskList5();
		subsIn[6] = createTaskList6();

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(18, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList1()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(18, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList2()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(18, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList3()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(19, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList4()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(19, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList5()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(20, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	TaskList createTaskList6()
	{
		TaskList retVal;

		retVal = new TaskList(3, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(17, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, true));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(20, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), false, true));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(21, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), TermList.NIL))), false, true));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new Precondition25(unifier)).setComparator(null);
			break;
			case 1:
				p = (new Precondition26(unifier)).setComparator(null);
			break;
			case 2:
				p = (new Precondition27(unifier)).setComparator(null);
			break;
			case 3:
				p = (new Precondition28(unifier)).setComparator(null);
			break;
			case 4:
				p = (new Precondition29(unifier)).setComparator(null);
			break;
			case 5:
				p = (new Precondition30(unifier)).setComparator(null);
			break;
			case 6:
				p = (new Precondition31(unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method6Branch0";
			case 1: return "Method6Branch1";
			case 2: return "Method6Branch2";
			case 3: return "Method6Branch3";
			case 4: return "Method6Branch4";
			case 5: return "Method6Branch5";
			case 6: return "Method6Branch6";
			default: return null;
		}
	}
}

class Method7 extends Method
{
	public Method7()
	{
		super(new Predicate(6, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)));
		TaskList[] subsIn = new TaskList[1];

		subsIn[0] = createTaskList0();

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(1, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(22, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), false, true));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionNegation(new PreconditionAtomic(new Predicate(37, 1, new TermList(TermVariable.getVariable(0), TermList.NIL)), unifier), 1)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method7Branch0";
			default: return null;
		}
	}
}

class Method8 extends Method
{
	public Method8()
	{
		super(new Predicate(7, 5, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(3), new TermList(TermVariable.getVariable(4), TermList.NIL)))))));
		TaskList[] subsIn = new TaskList[1];

		subsIn[0] = createTaskList0();

		setSubs(subsIn);
	}

	TaskList createTaskList0()
	{
		TaskList retVal;

		retVal = new TaskList(4, true);
		retVal.subtasks[0] = new TaskList(new TaskAtom(new Predicate(0, 5, new TermList(TermVariable.getVariable(2), TermList.NIL)), false, false));
		retVal.subtasks[1] = new TaskList(new TaskAtom(new Predicate(4, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(1), TermList.NIL))), false, false));
		retVal.subtasks[2] = new TaskList(new TaskAtom(new Predicate(5, 5, new TermList(TermVariable.getVariable(2), new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(3), new TermList(TermVariable.getVariable(4), TermList.NIL))))), false, false));
		retVal.subtasks[3] = new TaskList(new TaskAtom(new Predicate(6, 5, new TermList(TermVariable.getVariable(4), TermList.NIL)), false, false));

		return retVal;
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAtomic(new Predicate(73, 5, new TermList(TermVariable.getVariable(0), new TermList(TermConstant.getConstant(72), TermList.NIL))), unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Method8Branch0";
			default: return null;
		}
	}
}

class Axiom0 extends Axiom
{
	public Axiom0()
	{
		super(new Predicate(0, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAssign(new TermCall(new List(TermVariable.getVariable(1), TermList.NIL), datamining.calculateGetHead, "datamining.calculateGetHead"), unifier, 0)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom0Branch0";
			default: return null;
		}
	}
}

class Axiom1 extends Axiom
{
	public Axiom1()
	{
		super(new Predicate(1, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAssign(new TermCall(new List(TermVariable.getVariable(1), TermList.NIL), datamining.calculateGetTail, "datamining.calculateGetTail"), unifier, 0)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom1Branch0";
			default: return null;
		}
	}
}

class Axiom2 extends Axiom
{
	public Axiom2()
	{
		super(new Predicate(2, 1, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(0), TermList.NIL))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionNil(1)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom2Branch0";
			default: return null;
		}
	}
}

class Axiom3 extends Axiom
{
	public Axiom3()
	{
		super(new Predicate(3, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionNegation(new PreconditionAtomic(new Predicate(2, 2, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier), 2)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom3Branch0";
			default: return null;
		}
	}
}

class Axiom4 extends Axiom
{
	public Axiom4()
	{
		super(new Predicate(4, 3, new TermList(TermVariable.getVariable(0), new TermList(new TermList(TermVariable.getVariable(1), TermVariable.getVariable(2)), TermList.NIL))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAtomic(new Predicate(2, 3, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom4Branch0";
			default: return null;
		}
	}
}

class Precondition0 extends Precondition
{
	Precondition[] p;
	Term[][] b;

	public Precondition0(Term[] unifier)
	{
		p = new Precondition[3];
		p[1] = new PreconditionAtomic(new Predicate(3, 3, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), TermList.NIL))), unifier);
		p[2] = new PreconditionAtomic(new Predicate(4, 3, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(2), TermList.NIL))), unifier);
		b = new Term[3][];
		b[0] = unifier;
		b[0] = Term.merge( b, 1 );

		setFirst(false);
	}

	public void bind(Term[] binding)
	{
		b[0] = binding;
		b[0] = Term.merge( b, 1 );
		p[1].bind(binding);
		b[1] = null;
		b[2] = null;
	}

	protected Term[] nextBindingHelper()
	{
		while (b[2] == null)
		{
			boolean b1changed = false;
			while (b[1] == null)
			{
				b[1] = p[1].nextBinding();
				if (b[1] == null)
					return null;
				b1changed = true;
			}
			if ( b1changed ) {
				p[2].reset();
				p[2].bind(Term.merge(b, 2));
			}
			b[2] = p[2].nextBinding();
			if (b[2] == null) b[1] = null;
		}

		Term[] retVal = Term.merge(b, 3);
		b[2] = null;
		return retVal;
	}

	protected void resetHelper()
	{
		p[1].reset();
		p[2].reset();
		b[1] = null;
		b[2] = null;
	}
}

class Axiom5 extends Axiom
{
	public Axiom5()
	{
		super(new Predicate(4, 3, new TermList(TermVariable.getVariable(0), new TermList(new TermList(TermVariable.getVariable(1), TermVariable.getVariable(2)), TermList.NIL))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new Precondition0(unifier)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom5Branch0";
			default: return null;
		}
	}
}

class Axiom6 extends Axiom
{
	public Axiom6()
	{
		super(new Predicate(5, 3, new TermList(TermVariable.getVariable(0), new TermList(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), TermList.NIL)))), 1);
	}

	public Precondition getIterator(Term[] unifier, int which)
	{
		Precondition p;

		switch (which)
		{
			case 0:
				p = (new PreconditionAssign(new TermCall(new List(TermVariable.getVariable(1), new TermList(TermVariable.getVariable(2), TermList.NIL)), datamining.calculateInitializeOperator, "datamining.calculateInitializeOperator"), unifier, 0)).setComparator(null);
			break;
			default:
				return null;
		}

		p.reset();

		return p;
	}

	public String getLabel(int which)
	{
		switch (which)
		{
			case 0: return "Axiom6Branch0";
			default: return null;
		}
	}
}

public class datamining extends Domain
{
	public static GetHead calculateGetHead = new GetHead();

	public static GetTail calculateGetTail = new GetTail();

	public static InitializeOperator calculateInitializeOperator = new InitializeOperator();

	public static CheckListEmpty calculateCheckListEmpty = new CheckListEmpty();

	public datamining()
	{
		TermVariable.initialize(5);

		constants = new String[74];
		constants[0] = "get-head";
		constants[1] = "get-tail";
		constants[2] = "same";
		constants[3] = "different";
		constants[4] = "have-in-list";
		constants[5] = "initialize-operator";
		constants[6] = "has-file-name";
		constants[7] = "load-csv-operator";
		constants[8] = "is-loaded";
		constants[9] = "load-arff-operator";
		constants[10] = "randomize-operator";
		constants[11] = "is-shuffled";
		constants[12] = "has-name";
		constants[13] = "class-assigner-operator";
		constants[14] = "assign-target-class";
		constants[15] = "has-feature";
		constants[16] = "replace-missing-value-operator";
		constants[17] = "has-missing-percentage";
		constants[18] = "is-imputed";
		constants[19] = "has-index";
		constants[20] = "remove-feature-operator";
		constants[21] = "cfs-subset-eval-operator";
		constants[22] = "is-reduced-dimension";
		constants[23] = "correlation-attribute-eval-operator";
		constants[24] = "gain-ratio-attribute-eval-operator";
		constants[25] = "info-gain-attribute-eval-operator";
		constants[26] = "relieff-attribute-eval-operator";
		constants[27] = "symmetrical-uncert-attribute-eval-operator";
		constants[28] = "wrapper-subset-eval-operator";
		constants[29] = "standardization-operator";
		constants[30] = "is-standardized";
		constants[31] = "set-binary-classification-operator";
		constants[32] = "has-algorithm-name";
		constants[33] = "cross-validation-fold-maker-operator";
		constants[34] = "set-evaluation-method";
		constants[35] = "crossvalidation";
		constants[36] = "has-evaluation-method";
		constants[37] = "is-empty";
		constants[38] = "j48-operator";
		constants[39] = "has-data-table";
		constants[40] = "j48";
		constants[41] = "has-data-mining-algorithm";
		constants[42] = "random-forest-operator";
		constants[43] = "randomforest";
		constants[44] = "neural-network-operator";
		constants[45] = "neuralnetwork";
		constants[46] = "classifier-performance-evaluator-operator";
		constants[47] = "result-viewer-operator";
		constants[48] = "csv";
		constants[49] = "has-input-data-file-type";
		constants[50] = "has-target-class";
		constants[51] = "arff";
		constants[52] = "has-missing-value";
		constants[53] = "has-number-of-features";
		constants[54] = "has-number-of-data-points";
		constants[55] = "has-neuralnetwork-algorithm-requirement";
		constants[56] = "cfs-subset-eval";
		constants[57] = "has-attribute-selection-algorithm";
		constants[58] = "correlation-attribute-eval";
		constants[59] = "gain-ratio-attribute-eval";
		constants[60] = "info-gain-attribute-eval";
		constants[61] = "relieff-attribute-eval";
		constants[62] = "symmetrical-uncert-attribute-eval";
		constants[63] = "wrapper-subset-eval";
		constants[64] = "numerical";
		constants[65] = "contains-data-type";
		constants[66] = "has-missing-values";
		constants[67] = "has-j48-algorithm-requirement";
		constants[68] = "has-speed-requirement";
		constants[69] = "has-interpretability-requirement";
		constants[70] = "has-randomforest-algorithm-requirement";
		constants[71] = "has-variable-importance-requirement";
		constants[72] = "binaryclassificationtask";
		constants[73] = "has-val";

		compoundTasks = new String[8];
		compoundTasks[0] = "data-preparation-method";
		compoundTasks[1] = "missing-value-handling-method";
		compoundTasks[2] = "attribute-selection-handling-method";
		compoundTasks[3] = "data-standardization-method";
		compoundTasks[4] = "data-preprocessing-method";
		compoundTasks[5] = "binary-classification-modeling-method";
		compoundTasks[6] = "report-method";
		compoundTasks[7] = "generate-data-mining-workflow";

		primitiveTasks = new String[23];
		primitiveTasks[0] = "!!add-state-to-the-world";
		primitiveTasks[1] = "!!remove-state-from-the-world";
		primitiveTasks[2] = "!load-csv-operator";
		primitiveTasks[3] = "!load-arff-operator";
		primitiveTasks[4] = "!randomize-operator";
		primitiveTasks[5] = "!class-assigner-operator";
		primitiveTasks[6] = "!replace-missing-value-operator";
		primitiveTasks[7] = "!remove-feature-operator";
		primitiveTasks[8] = "!cfs-subset-eval-operator";
		primitiveTasks[9] = "!correlation-attribute-eval-operator";
		primitiveTasks[10] = "!gain-ratio-attribute-eval-operator";
		primitiveTasks[11] = "!info-gain-attribute-eval-operator";
		primitiveTasks[12] = "!relieff-attribute-eval-operator";
		primitiveTasks[13] = "!symmetrical-uncert-attribute-eval-operator";
		primitiveTasks[14] = "!wrapper-subset-eval-operator";
		primitiveTasks[15] = "!standardization-operator";
		primitiveTasks[16] = "!set-binary-classification-algorithm-operator";
		primitiveTasks[17] = "!cross-validation-fold-maker-operator";
		primitiveTasks[18] = "!j48-operator";
		primitiveTasks[19] = "!random-forest-operator";
		primitiveTasks[20] = "!neural-network-operator";
		primitiveTasks[21] = "!classifier-performance-evaluator-operator";
		primitiveTasks[22] = "!result-viewer-operator";

		methods = new Method[8][];

		methods[0] = new Method[1];
		methods[0][0] = new Method0();

		methods[1] = new Method[2];
		methods[1][0] = new Method1();
		methods[1][1] = new Method2();

		methods[2] = new Method[1];
		methods[2][0] = new Method3();

		methods[3] = new Method[1];
		methods[3][0] = new Method4();

		methods[4] = new Method[1];
		methods[4][0] = new Method5();

		methods[5] = new Method[1];
		methods[5][0] = new Method6();

		methods[6] = new Method[1];
		methods[6][0] = new Method7();

		methods[7] = new Method[1];
		methods[7][0] = new Method8();


		ops = new Operator[23][];

		ops[0] = new Operator[1];
		ops[0][0] = new Operator0();

		ops[1] = new Operator[1];
		ops[1][0] = new Operator1();

		ops[2] = new Operator[1];
		ops[2][0] = new Operator2();

		ops[3] = new Operator[1];
		ops[3][0] = new Operator3();

		ops[4] = new Operator[1];
		ops[4][0] = new Operator4();

		ops[5] = new Operator[1];
		ops[5][0] = new Operator5();

		ops[6] = new Operator[1];
		ops[6][0] = new Operator6();

		ops[7] = new Operator[1];
		ops[7][0] = new Operator7();

		ops[8] = new Operator[1];
		ops[8][0] = new Operator8();

		ops[9] = new Operator[1];
		ops[9][0] = new Operator9();

		ops[10] = new Operator[1];
		ops[10][0] = new Operator10();

		ops[11] = new Operator[1];
		ops[11][0] = new Operator11();

		ops[12] = new Operator[1];
		ops[12][0] = new Operator12();

		ops[13] = new Operator[1];
		ops[13][0] = new Operator13();

		ops[14] = new Operator[1];
		ops[14][0] = new Operator14();

		ops[15] = new Operator[1];
		ops[15][0] = new Operator15();

		ops[16] = new Operator[1];
		ops[16][0] = new Operator16();

		ops[17] = new Operator[1];
		ops[17][0] = new Operator17();

		ops[18] = new Operator[1];
		ops[18][0] = new Operator18();

		ops[19] = new Operator[1];
		ops[19][0] = new Operator19();

		ops[20] = new Operator[1];
		ops[20][0] = new Operator20();

		ops[21] = new Operator[1];
		ops[21][0] = new Operator21();

		ops[22] = new Operator[1];
		ops[22][0] = new Operator22();

		axioms = new Axiom[74][];

		axioms[0] = new Axiom[1];
		axioms[0][0] = new Axiom0();

		axioms[1] = new Axiom[1];
		axioms[1][0] = new Axiom1();

		axioms[2] = new Axiom[1];
		axioms[2][0] = new Axiom2();

		axioms[3] = new Axiom[1];
		axioms[3][0] = new Axiom3();

		axioms[4] = new Axiom[2];
		axioms[4][0] = new Axiom4();
		axioms[4][1] = new Axiom5();

		axioms[5] = new Axiom[1];
		axioms[5][0] = new Axiom6();

		axioms[6] = new Axiom[0];

		axioms[7] = new Axiom[0];

		axioms[8] = new Axiom[0];

		axioms[9] = new Axiom[0];

		axioms[10] = new Axiom[0];

		axioms[11] = new Axiom[0];

		axioms[12] = new Axiom[0];

		axioms[13] = new Axiom[0];

		axioms[14] = new Axiom[0];

		axioms[15] = new Axiom[0];

		axioms[16] = new Axiom[0];

		axioms[17] = new Axiom[0];

		axioms[18] = new Axiom[0];

		axioms[19] = new Axiom[0];

		axioms[20] = new Axiom[0];

		axioms[21] = new Axiom[0];

		axioms[22] = new Axiom[0];

		axioms[23] = new Axiom[0];

		axioms[24] = new Axiom[0];

		axioms[25] = new Axiom[0];

		axioms[26] = new Axiom[0];

		axioms[27] = new Axiom[0];

		axioms[28] = new Axiom[0];

		axioms[29] = new Axiom[0];

		axioms[30] = new Axiom[0];

		axioms[31] = new Axiom[0];

		axioms[32] = new Axiom[0];

		axioms[33] = new Axiom[0];

		axioms[34] = new Axiom[0];

		axioms[35] = new Axiom[0];

		axioms[36] = new Axiom[0];

		axioms[37] = new Axiom[0];

		axioms[38] = new Axiom[0];

		axioms[39] = new Axiom[0];

		axioms[40] = new Axiom[0];

		axioms[41] = new Axiom[0];

		axioms[42] = new Axiom[0];

		axioms[43] = new Axiom[0];

		axioms[44] = new Axiom[0];

		axioms[45] = new Axiom[0];

		axioms[46] = new Axiom[0];

		axioms[47] = new Axiom[0];

		axioms[48] = new Axiom[0];

		axioms[49] = new Axiom[0];

		axioms[50] = new Axiom[0];

		axioms[51] = new Axiom[0];

		axioms[52] = new Axiom[0];

		axioms[53] = new Axiom[0];

		axioms[54] = new Axiom[0];

		axioms[55] = new Axiom[0];

		axioms[56] = new Axiom[0];

		axioms[57] = new Axiom[0];

		axioms[58] = new Axiom[0];

		axioms[59] = new Axiom[0];

		axioms[60] = new Axiom[0];

		axioms[61] = new Axiom[0];

		axioms[62] = new Axiom[0];

		axioms[63] = new Axiom[0];

		axioms[64] = new Axiom[0];

		axioms[65] = new Axiom[0];

		axioms[66] = new Axiom[0];

		axioms[67] = new Axiom[0];

		axioms[68] = new Axiom[0];

		axioms[69] = new Axiom[0];

		axioms[70] = new Axiom[0];

		axioms[71] = new Axiom[0];

		axioms[72] = new Axiom[0];

		axioms[73] = new Axiom[0];

	}
}