package checkers.enums;

public enum Direction
{
	NW(0x07f7f7f7, 0x00777777, 5, 4, 0, 0, 9, 0),
	NE(0x0fefefef, 0x00eeeeee, 4, 3, 0, 0, 7, 0),
	SW(0xf7f7f7f0, 0x77777700, 0, 0, 3, 4, 0, 7),
	SE(0xefefefe0, 0xeeeeee00, 0, 0, 4, 5, 0, 9);

	public int moves;
	public int jumps;
	public int move_shl_e;
	public int move_shl_o;
	public int move_shr_e;
	public int move_shr_o;
	public int jump_shl;
	public int jump_shr;

	Direction(int m, int j, int mle, int mlo, int mre, int mro, int jl, int jr)
	{
		moves = m;
		jumps = j;
		move_shl_e = mle;
		move_shr_e = mre;
		move_shl_o = mlo;
		move_shr_o = mro;
		jump_shl = jl;
		jump_shr = jr;
	}
}
