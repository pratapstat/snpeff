package ca.mcgill.mcb.pcingola.snpSql.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Tuple extends Pojo<Tuple> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	String name;
	String value;

	@ManyToOne
	VcfEntryDb vcfEntryDb;

	/**
	 * Load (do it right now, not lazy)
	 *
	 * @param id
	 * @return
	 */
	public static Tuple get(long id) {
		return (Tuple) HibernateUtil.getCurrentSession().get(Tuple.class, id);
	}

	public Tuple() {
		id = null;
	}

	public Tuple(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public void copySimpleValues(Pojo c) {
		Tuple t = (Tuple) c;
		name = t.name;
		value = t.value;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public String getValueStr() {
		return value;
	}

	public VcfEntryDb getVcfEntryDb() {
		return vcfEntryDb;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setVcfEntryDb(VcfEntryDb vcfEntryDb) {
		this.vcfEntryDb = vcfEntryDb;
	}

	@Override
	public String toString() {
		return "TupleStr[" + id + "] :\t" + name + " = " + value;
	}
}
