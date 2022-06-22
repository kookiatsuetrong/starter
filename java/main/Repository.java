package main;
import data.Member;
import data.Setting;
import data.Activate;
import data.Reset;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
interface SettingRepository extends CrudRepository<Setting, Integer> {

}

@Repository
interface MemberRepository extends CrudRepository<Member, Integer> {
	Member findByCode(int code);
	Member findByEmail(String email);
}

@Repository
interface ActivateRepository extends CrudRepository<Activate, Integer> {
	Activate findBySecret(String secret);
}

@Repository
interface ResetRepository extends CrudRepository<Reset, Integer> {
	Reset findBySecret(String secret);
}
