package ca.mcgill.mcb.pcingola.snpSql.db;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

@Entity
public class VcfEntryDb extends Pojo<VcfEntryDb> {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	String chr;
	int pos;
	String vcfId;
	String ref, alt;
	double qual;
	String filter;

	@OneToMany(mappedBy = "vcfEntryDb")
	Set<Tuple> tuples;

	/**
	 * Load (do it right now, not lazy)
	 *
	 * @param id
	 * @return
	 */
	public static VcfEntryDb get(long id) {
		return (VcfEntryDb) HibernateUtil.getCurrentSession().get(VcfEntryDb.class, id);
	}

	public VcfEntryDb() {
		id = null;
		tuples = new HashSet<Tuple>();
	}

	public VcfEntryDb(VcfEntry vcfEntry) {
		chr = vcfEntry.getChromosomeName();
		pos = vcfEntry.getStart();
		vcfId = vcfEntry.getId();
		ref = vcfEntry.getRef();
		alt = vcfEntry.getAltsStr();
		qual = vcfEntry.getQuality();
		filter = vcfEntry.getFilterPass();
		tuples = new HashSet<Tuple>();
	}

	/**
	 * Add a tuple
	 * @param t
	 */
	public void add(Tuple t) {
		if (!tuples.contains(t)) tuples.add(t);
		t.setVcfEntryDb(this);
	}

	@Override
	public void copySimpleValues(Pojo c) {
		throw new RuntimeException("Unimplemented!");
	}

	public String getAlt() {
		return alt;
	}

	public String getChr() {
		return chr;
	}

	public String getFilter() {
		return filter;
	}

	@Override
	public Long getId() {
		return null;
	}

	public int getPos() {
		return pos;
	}

	public double getQual() {
		return qual;
	}

	public String getRef() {
		return ref;
	}

	public Set<Tuple> getTuples() {
		return tuples;
	}

	public String getVcfId() {
		return vcfId;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public void setChr(String chr) {
		this.chr = chr;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public void setQual(double qual) {
		this.qual = qual;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public void setTuples(Set<Tuple> tuples) {
		this.tuples = tuples;
	}

	public void setVcfId(String vcfId) {
		this.vcfId = vcfId;
	}

	@Override
	public String toString() {
		return "Position[" + id + "]=" + chr + ":" + pos + "\t" + vcfId + "\t" + ref + "\t" + alt + "\t" + filter;
	}
}
