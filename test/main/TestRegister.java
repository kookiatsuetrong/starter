package main;
import data.Member;
import data.Activate;

public class TestRegister {
	
	public TestRegister() {
		repository = Start.context.getBean(MemberRepository.class);
		activateRepository = Start.context.getBean(ActivateRepository.class);
	}
	
	MemberRepository repository;
	ActivateRepository activateRepository;
	
	public void testInsertMember() {
		Member m     = new Member();
		int random   = (int)(Math.random() * 100000);
		m.email      = "user-" + random + "@email.com";
		m.firstName  = "User";
		m.familyName = "Family";
		m.password   = Tool.encrypt("user123");
		m.status     = "tested";
		Member r = repository.save(m);
		assert r != null;
		assert m.code == r.code;
		
		Activate a = new Activate();
		a.member  = r.code;
		a.secret  = Tool.random(12);
		Activate c = activateRepository.save(a);
		assert c != null;
		
		boolean success = true;
		
		try {
			Activate t = activateRepository.findBySecret(a.secret);
			if (t == null) {
				throw new Exception();
			}
			Member u = repository.findByCode(a.member);
			if (u == null) {
				throw new Exception();
			}
			u.status = "member";
			Member v = repository.save(u);
			assert v.status.equals("member");
			activateRepository.deleteById(a.code);
		} catch (Exception e) { 
			success = false;
		}
		assert success;
	}
}
